# WebSocket Setup — Laravel Reverb dan Echo

Chat menggunakan event `ShouldBroadcastNow`, private channel `conversation.{conversationId}`, Laravel Echo, dan polling fallback.

## Instalasi Laravel 13

Jalankan installer broadcasting:

```bash
php artisan install:broadcasting
```

Pada pilihan driver, gunakan tombol panah untuk memilih `Laravel Reverb`, lalu tekan Enter. Jangan mengetik nama menu secara manual.

Apabila Reverb belum tersedia pada dependencies:

```bash
composer require laravel/reverb
php artisan install:broadcasting
```

## Environment Backend

```env
BROADCAST_CONNECTION=reverb

REVERB_APP_ID=ziip-marketplace
REVERB_APP_KEY=ziip-marketplace-key
REVERB_APP_SECRET=ganti-dengan-secret-aman
REVERB_HOST=127.0.0.1
REVERB_PORT=8080
REVERB_SCHEME=http
```

Setelah mengubah environment:

```bash
php artisan optimize:clear
php artisan reverb:start
```

## Environment Frontend

```env
VITE_API_BASE_URL=http://127.0.0.1:8000
VITE_REVERB_APP_KEY=ziip-marketplace-key
VITE_REVERB_HOST=127.0.0.1
VITE_REVERB_PORT=8080
VITE_REVERB_FORCE_TLS=false
```

Install dependency:

```bash
npm install laravel-echo pusher-js
```

## Authorization

Endpoint auth channel:

```text
/broadcasting/auth
```

Request menggunakan bearer token Sanctum. Admin active role dapat mengakses semua conversation. Buyer dan seller hanya dapat subscribe apabila menjadi participant aktif.

## Event

```text
Private channel: conversation.{conversationId}
Event name: .chat.message.sent
```

Payload pesan memuat:

- `sender_id`
- `sender_name`
- `sender_avatar`
- `sender_identity_type`
- `message_type`
- `message`
- `attachments`
- `created_at`

Untuk pesan dari owner toko, `sender_name`, `sender_avatar`, dan `sender_identity_type` menggunakan identitas toko.

## Polling Fallback

- List conversation: 10 detik ketika websocket tidak connected.
- Detail conversation: 5 detik ketika websocket tidak connected.
- Polling berhenti ketika status Reverb connected.

## Troubleshooting

### Prompt Driver Mengulang

Gunakan tombol panah lalu Enter. Nilai kosong atau teks seperti `Larave Reverb` tidak cocok dengan pilihan prompt.

### 403 Broadcasting Auth

Jalankan:

```bash
php artisan db:seed --class=RolePermissionSeeder
php artisan optimize:clear
```

Pastikan user memiliki `chat.use`, token berisi active role yang benar, serta origin frontend diterima Sanctum dan CORS.

### Pesan Masuk Hanya Setelah Refresh

Periksa:

```bash
php artisan reverb:start
```

Pastikan key, host, port, TLS frontend sama dengan Reverb backend. UI tetap memakai polling ketika koneksi gagal.
