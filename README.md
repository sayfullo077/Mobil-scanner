# DeviceControl

O'z Android telefoningizni test qilish uchun sodda boshqaruv ilovasi.
Barcha funksiyalar **faqat ilova o'rnatilgan telefonning o'zi** uchun ishlaydi va
hech qanday ma'lumot tashqi serverga yuborilmaydi — hammasi lokal.

## Texnologiyalar
- Kotlin + Jetpack Compose (Material 3)
- Clean Architecture: `domain` / `data` / `presentation` qatlamlari
- Hilt (dependency injection)
- Coroutines + Flow / StateFlow
- Jetpack Navigation (Compose)

## Funksiyalar (MVP)
1. **Tizim** — batareya, RAM, xotira (storage), protsessor yadrolari, model, Android versiyasi (real vaqtda).
2. **Tarmoq** — Wi-Fi holati va atrofdagi tarmoqlar skaneri, Bluetooth qurilmalarni skaner qilish.
3. **Sozlama** — ekran yorqinligi, media ovoz balandligi, uchish rejimi sozlamalariga o'tish.
4. **Ilovalar** — o'rnatilgan ilovalar ro'yxati va har birining so'ragan ruxsatlari, ilovani ochish.
5. **Fayllar** — ilovaning xavfsiz hududida (scoped storage) fayl/papka yaratish, ko'rish, o'chirish.
6. **Xabar** — namuna bildirishnoma yuborish.

## Ruxsatlar — nima uchun kerak
| Ruxsat | Sabab |
|--------|-------|
| `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE` | Wi-Fi holati va skaner |
| `ACCESS_FINE_LOCATION` | Android Wi-Fi skaneri uchun joylashuvni majburiy qiladi |
| `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` | Bluetooth qurilmalarni topish (Android 12+) |
| `POST_NOTIFICATIONS` | Bildirishnoma yuborish (Android 13+) |
| `QUERY_ALL_PACKAGES` | O'rnatilgan ilovalar ro'yxati |
| `WRITE_SETTINGS` | Ekran yorqinligini o'zgartirish (foydalanuvchi alohida ruxsat beradi) |

Har bir xavfli ruxsat **runtime'da** so'raladi va ilova ichida nima uchun kerakligi
tushuntiriladi.

## Build qilish (Android Studio)
1. Android Studio (Hedgehog yoki undan yangi) ni oching.
2. `File > Open` orqali shu papkani tanlang.
3. Android Studio Gradle sync paytida kerakli Android SDK (compileSdk 35) va
   kutubxonalarni avtomatik yuklab oladi.
4. Telefoningizni USB orqali ulang (Developer options > USB debugging yoqilgan bo'lsin)
   yoki emulyator ishga tushiring.
5. `Run ▶` tugmasini bosing — ilova telefonga o'rnatiladi.

## Build qilish (terminal)
Android SDK o'rnatilgan va `local.properties` da `sdk.dir` ko'rsatilgan bo'lsa:
```
./gradlew assembleDebug
```
Tayyor APK: `app/build/outputs/apk/debug/app-debug.apk`

## APK ni telefonga qo'lda o'rnatish
1. `app-debug.apk` ni telefonga ko'chiring.
2. Telefonda `Settings > Security > Noma'lum manbalardan o'rnatish` ni yoqing
   (yoki o'rnatishda so'ralganda ruxsat bering).
3. APK ustiga bosib o'rnating.

## Eslatma
- `minSdk = 26` (Android 8.0), `targetSdk = 35`, `compileSdk = 35`.
- Bu ilova o'quv/test maqsadida, faqat o'z qurilmangiz uchun mo'ljallangan.
