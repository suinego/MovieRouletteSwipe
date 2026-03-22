package com.example.movieroulette

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.movieroulette.di.databaseModule
import com.example.movieroulette.di.networkModule
import com.example.movieroulette.di.repositoryModule
import com.example.movieroulette.di.viewModelModule
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.net.InetAddress
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MovieRouletteApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieRouletteApplication)
            modules(listOf(networkModule, databaseModule, repositoryModule, viewModelModule))
        }
    }

    override fun newImageLoader(): ImageLoader {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("CoilHttp", message)
        }.apply { level = HttpLoggingInterceptor.Level.BASIC }

        return ImageLoader.Builder(this)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .dns(TmdbImageDns())
                    .build()
            )
            .build()
    }
}
private class TmdbImageDns : Dns {

    private val bootstrapClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private val dohServers: List<DnsOverHttps> = listOf(
        "https://cloudflare-dns.com/dns-query",
        "https://dns.google/dns-query",
        "https://1.1.1.1/dns-query",
        "https://1.0.0.1/dns-query",
        "https://8.8.8.8/dns-query",
        "https://8.8.4.4/dns-query",
        "https://9.9.9.9/dns-query",
        "https://94.140.14.14/dns-query",
    ).map { url ->
        DnsOverHttps.Builder()
            .client(bootstrapClient)
            .url(url.toHttpUrl())
            .includeIPv6(false)
            .build()
    }

    private val fallbackIps = listOf(
        "84.17.38.231",
        "18.172.213.119",
    )

    override fun lookup(hostname: String): List<InetAddress> {
        if (hostname != "image.tmdb.org") return Dns.SYSTEM.lookup(hostname)

        val executor = Executors.newFixedThreadPool(dohServers.size)
        val future = CompletableFuture<List<InetAddress>>()

        for (doh in dohServers) {
            executor.submit {
                if (future.isDone) return@submit
                try {
                    val ips = doh.lookup(hostname).filter { !it.isLoopbackAddress }
                    if (ips.isNotEmpty() && !future.isDone) {
                        Log.d("ImageDns", "f ${doh}: $ips")
                        future.complete(ips)
                    }
                } catch (_: Exception) {}
            }
        }

        executor.shutdown()

        return try {
            future.get(10, TimeUnit.SECONDS)
        } catch (e: Exception) {
            Log.w("ImageDns", "fff")
            executor.shutdownNow()
            fallbackIps.mapNotNull {
                try { InetAddress.getByName(it) } catch (_: Exception) { null }
            }
        }
    }
}
