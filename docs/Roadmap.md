# To-Do List Marketplace

## Status Perubahan Terakhir

Terakhir diperbarui: 26 Agustus 2026

### Perubahan yang Dilakukan

**Backend (market-api):**
- [x] Fix IDOR pada user profiles (ownership check)
- [x] Fix voucher show endpoint (added auth middleware)
- [x] Fix duplicate payment records (idempotency check)
- [x] Fix race condition stock decrement (lockForUpdate dalam transaksi)
- [x] Fix import Excel (gunakan nama/code bukan ID)
- [x] Tambahkan verified.email middleware ke order/cart/wishlist/address routes
- [x] Tambahkan endpoint POST /engagement/missions/report untuk game completion

**Frontend (market-frontend):**
- [x] Fix LoadingScreen (spinner bukan null)
- [x] Fix CartSummary (gunakan shipping/discount aktual)
- [x] Fix ReviewTab (load data review sesungguhnya)
- [x] Fix staleTime seller products (30 detik)
- [x] Fix useConversation polling (15 detik bukan 5 detik)
- [x] Fix seller onboarding missing fields (whatsappUrl, tiktokUrl)
- [x] Tambahkan ErrorBoundary component
- [x] Fix image URL consistency (skip localhost redirect di dev)

**Android (SDGS):**
- [x] Tambahkan game completion → mission report API
- [x] Integrasi QuizViewModel dengan backend mission
- [x] Integrasi TrashSortViewModel dengan backend mission
- [x] Integrasi MythFactViewModel dengan backend mission
- [x] Integrasi MatchCardViewModel dengan backend mission
- [x] Integrasi CleanRiverViewModel dengan backend mission
- [x] Tambahkan POST /engagement/missions/report endpoint di ApiService

---

## 0. Fondasi Sistem

### Authentication & Authorization

* [x] Register
* [x] Login
* [x] Logout
* [x] Forgot/reset password
* [x] Login dengan Google
* [x] Verifikasi email jika diperlukan
* [x] Role & permission: Buyer, Seller, Admin
* [x] Middleware/guard berdasarkan role
* [x] Manajemen session/token
* [x] Ban/suspend user

### User Profile

* [x] CRUD profile user
* [ ] Upload foto profile
* [x] Edit biodata
* [x] CRUD alamat
* [x] Menentukan alamat utama
* [x] Menyimpan nomor telepon
* [x] Menyimpan informasi tambahan user

### Catalog

* [x] CRUD Catalog Group
* [x] CRUD Category Level 1
* [x] CRUD Category Level 2
* [x] CRUD Category Level 3
* [x] Relasi Catalog Group → Category
* [x] Relasi Category parent → child
* [x] Menentukan status aktif/nonaktif category
* [x] Menampilkan catalog pada halaman marketplace
* [x] Search product
* [x] Filter product
* [x] Sort product

---

# 1. BUYER

## 1.1 Product Discovery

* [x] Melihat catalog
* [x] Melihat category
* [x] Melihat daftar product
* [x] Melihat detail product
* [x] Melihat foto product
* [x] Melihat variant product
* [x] Melihat stok
* [x] Melihat harga
* [x] Melihat informasi seller/toko
* [x] Search product
* [x] Filter product
* [x] Sort product

## 1.2 Wishlist

* [x] Tambah product ke wishlist
* [x] Hapus product dari wishlist
* [x] Melihat wishlist
* [x] Mengecek apakah product sudah ada di wishlist

## 1.3 Cart

* [x] Tambah product ke cart
* [x] Memilih variant product
* [x] Mengubah quantity
* [x] Menghapus product dari cart
* [x] Memilih product yang akan dibeli
* [x] Menghitung subtotal
* [x] Menggunakan voucher toko
* [x] Menggunakan voucher marketplace
* [x] Menghitung total pembayaran

## 1.4 Checkout & Order

* [x] Membuat checkout
* [x] Memilih alamat pengiriman
* [x] Memilih metode pengiriman
* [x] Memilih voucher
* [x] Menghitung total order
* [x] Membuat order
* [x] Membuat order item
* [x] Pembayaran
* [x] Menampilkan status pembayaran
* [x] Menampilkan status order
* [x] Cancel order sesuai kondisi
* [x] Melihat riwayat order
* [x] Melihat detail order
* [x] Konfirmasi penerimaan barang
* [x] Menyelesaikan order

### Status Order

* [x] Pending payment
* [x] Paid
* [x] Processing
* [x] Shipped
* [x] Delivered
* [x] Completed
* [x] Cancelled
* [ ] Refunded jika diperlukan

## 1.5 Review & Rating

* [x] Buyer hanya dapat review setelah order selesai
* [x] Memberikan rating
* [x] Menulis review
* [ ] Upload foto/video review jika diperlukan
* [x] Edit review
* [x] Hapus review
* [x] Melihat review product
* [x] Melihat rating seller
* [ ] Melaporkan review yang bermasalah

## 1.6 Chat Toko

* [x] Melihat daftar toko yang pernah di-chat
* [x] Membuat conversation dengan seller
* [x] Mengirim pesan
* [x] Menerima pesan
* [x] Menampilkan status read/unread
* [ ] Mengirim gambar jika diperlukan
* [x] Notifikasi pesan baru
* [ ] Block/report conversation jika diperlukan

## 1.7 Voucher

* [x] Melihat voucher yang tersedia
* [ ] Claim voucher
* [ ] Menyimpan voucher ke akun
* [x] Menggunakan voucher saat checkout
* [x] Validasi masa berlaku voucher
* [x] Validasi minimum pembelian
* [x] Validasi kuota voucher
* [x] Validasi voucher khusus toko
* [x] Validasi voucher marketplace

## 1.8 Game & Daily Mission

* [x] Melihat daily mission
* [x] Menyelesaikan mission
* [x] Mendapatkan reward
* [ ] Mendapatkan voucher dari reward
* [x] Menyimpan progress mission
* [x] Membatasi reward agar tidak dapat dieksploitasi
* [x] Reset mission sesuai periode
* [ ] Menampilkan history reward

## 1.9 Help Center

* [x] Buyer dapat membuat tiket bantuan
* [x] Memilih kategori masalah
* [x] Menjelaskan masalah
* [ ] Upload bukti jika diperlukan
* [x] Melihat status tiket
* [x] Membalas tiket
* [x] Menutup tiket
* [x] Melihat history bantuan

---

# 2. SELLER

## 2.1 Store Management

* [x] Membuat toko
* [x] Edit informasi toko
* [x] Upload logo toko
* [x] Upload banner toko
* [x] Mengatur deskripsi toko
* [x] Mengatur informasi toko
* [x] Mengatur jam operasional
* [x] Mengatur status toko
* [x] Melihat halaman publik toko

## 2.2 Product Management

### Product

* [x] CRUD product
* [x] Nama product
* [x] Deskripsi product
* [x] Category product
* [x] Harga jual
* [x] HPP
* [x] Status product
* [x] Berat/dimensi product
* [x] SKU

### Product Image

* [x] Upload gambar product
* [x] Multiple product images
* [x] Menentukan gambar utama
* [x] Menghapus gambar
* [x] Mengurutkan gambar

### Product Variant

* [x] Product tanpa variant
* [x] Product dengan variant
* [x] CRUD attribute
* [x] CRUD variant
* [x] Variant memiliki harga sendiri
* [x] Variant memiliki SKU sendiri
* [x] Variant memiliki stok sendiri
* [x] Variant memiliki HPP sendiri jika diperlukan

## 2.3 Showcase / Etalase

* [x] CRUD showcase
* [x] Membuat kelompok etalase
* [x] Menambahkan product ke showcase
* [x] Menghapus product dari showcase
* [x] Mengatur urutan product
* [x] Menampilkan showcase di halaman toko

Contoh:

> "Best Seller"
> "Promo Minggu Ini"
> "Menu Baru"
> "Rekomendasi"

## 2.4 Voucher Toko

* [x] CRUD voucher toko
* [x] Menentukan nominal/persentase diskon
* [x] Menentukan minimum pembelian
* [x] Menentukan maksimum diskon
* [x] Menentukan periode voucher
* [x] Menentukan kuota voucher
* [x] Menentukan product/category yang berlaku
* [x] Mengaktifkan/nonaktifkan voucher
* [x] Melihat penggunaan voucher

## 2.5 Promotion / Advertising

Seller dapat mengajukan product/toko untuk dipromosikan oleh marketplace.

* [x] Membuat campaign promotion
* [x] Memilih product yang dipromosikan
* [x] Menentukan budget
* [x] Menentukan periode promosi
* [x] Melakukan pembayaran promotion
* [x] Status: Draft
* [x] Status: Waiting Payment
* [x] Status: Waiting Approval
* [x] Status: Approved
* [x] Status: Rejected
* [x] Status: Running
* [x] Status: Completed
* [x] Seller dapat melihat performa promotion
* [x] Seller dapat melihat biaya promotion

### Admin Approval

* [x] Admin melihat pengajuan promotion
* [x] Admin memeriksa promotion
* [x] Admin approve
* [x] Admin reject
* [x] Admin memberikan alasan rejection
* [x] Sistem menjalankan promotion setelah approved

## 2.6 Review Management

* [x] Melihat review product
* [x] Melihat rating product
* [x] Melihat rating toko
* [x] Melihat review terbaru
* [ ] Melaporkan review bermasalah
* [ ] Melihat review yang dilaporkan
* [ ] Memberikan response terhadap review jika diperlukan

## 2.7 Financial Management

### Income

* [x] Mencatat pemasukan
* [x] Melihat pemasukan harian
* [x] Melihat pemasukan mingguan
* [x] Melihat pemasukan bulanan
* [x] Melihat pemasukan berdasarkan product

### Expense

* [x] CRUD pengeluaran
* [x] Kategori pengeluaran
* [x] Mencatat nominal
* [x] Mencatat tanggal
* [x] Mencatat keterangan

### Hutang & Piutang

* [x] CRUD hutang
* [x] CRUD piutang
* [x] Mencatat pihak terkait
* [x] Nominal
* [x] Jatuh tempo
* [x] Status pembayaran
* [x] History pembayaran

### Financial Summary

* [x] Total revenue
* [x] Total expense
* [x] Total HPP
* [x] Gross profit
* [x] Net profit
* [x] Hutang
* [x] Piutang
* [x] Laporan keuangan

## 2.8 Stock Management

### Master Bahan Baku

* [x] CRUD bahan baku
* [x] Nama bahan baku
* [x] Satuan
* [x] Harga beli
* [x] Supplier
* [x] Minimum stock
* [x] Status bahan baku

### Stock

* [x] Stock masuk
* [x] Stock keluar
* [x] Stock adjustment
* [x] Stock opname
* [ ] Low stock notification
* [ ] Out of stock notification

### Stock Movement

* [x] Mencatat setiap perubahan stock
* [x] Stock in
* [x] Stock out
* [x] Adjustment
* [x] Return
* [x] Menyimpan user yang melakukan perubahan
* [x] Menyimpan waktu perubahan
* [x] Menyimpan alasan perubahan

## 2.9 HPP / Cost Management

* [x] Menentukan HPP product
* [x] Menentukan komponen biaya product
* [x] Menghubungkan product dengan bahan baku
* [x] Menghitung biaya bahan baku
* [x] Menghitung biaya produksi
* [x] Mengubah harga bahan baku
* [x] Recalculate HPP ketika harga bahan baku berubah
* [x] Menyimpan history perubahan HPP
* [x] Menampilkan HPP saat ini
* [x] Menampilkan history HPP

Contoh:

> Harga tepung naik → harga bahan baku berubah → HPP product dihitung ulang → seller dapat menentukan kembali harga jual.

## 2.10 Seller Help

* [x] Seller membuat tiket bantuan
* [x] Memilih kategori masalah
* [x] Menjelaskan masalah
* [ ] Upload bukti
* [x] Melihat status tiket
* [x] Membalas tiket
* [x] Menutup tiket
* [x] Melihat history bantuan

---

# 3. ADMIN

## 3.1 Catalog Management

* [x] CRUD Catalog Group
* [x] CRUD Category Level 1
* [x] CRUD Category Level 2
* [x] CRUD Category Level 3
* [x] Mengatur parent category
* [x] Mengaktifkan/nonaktifkan category
* [x] Mengatur urutan category
* [x] Mengatur category yang tampil di marketplace

## 3.2 User Management

* [x] Melihat daftar user
* [x] Melihat detail user
* [ ] Melihat aktivitas user
* [x] Mengubah status user
* [x] Suspend user
* [x] Ban user
* [x] Mengaktifkan kembali user
* [ ] Menangani laporan user
* [ ] Melihat history tindakan terhadap user

## 3.3 Seller / Store Management

* [x] Melihat daftar seller
* [x] Melihat detail toko
* [x] Mengubah status toko
* [x] Suspend toko
* [x] Menonaktifkan toko
* [x] Mengaktifkan kembali toko
* [ ] Memeriksa pelanggaran toko
* [ ] Melihat history tindakan toko

## 3.4 Product Moderation

* [x] Melihat product seller
* [x] Review product yang bermasalah
* [x] Mengubah status product
* [x] Menonaktifkan product
* [x] Menghapus product yang melanggar aturan
* [ ] Memberikan alasan tindakan
* [ ] Melihat history moderasi product

## 3.5 Promotion Management

* [x] Melihat pengajuan promotion
* [x] Review campaign
* [x] Approve promotion
* [x] Reject promotion
* [x] Memberikan alasan rejection
* [x] Mengubah status promotion
* [ ] Menghentikan promotion bermasalah
* [ ] Melihat history promotion

## 3.6 Voucher Management

* [x] CRUD voucher marketplace
* [x] Menentukan nominal/persentase
* [x] Menentukan periode
* [x] Menentukan kuota
* [x] Menentukan minimum transaksi
* [x] Menentukan maximum discount
* [x] Mengaktifkan/nonaktifkan voucher
* [x] Melihat penggunaan voucher

## 3.7 Daily Mission Management

* [x] CRUD daily mission
* [x] Menentukan nama mission
* [x] Menentukan deskripsi
* [x] Menentukan requirement
* [x] Menentukan reward
* [x] Menentukan voucher reward
* [x] Menentukan periode mission
* [x] Mengaktifkan/nonaktifkan mission
* [x] Melihat progress mission user

## 3.8 Review Moderation

* [x] Melihat review
* [ ] Melihat review yang dilaporkan
* [ ] Memeriksa review
* [ ] Menghapus review yang melanggar aturan
* [ ] Mengubah status review
* [ ] Memberikan alasan moderasi
* [ ] Melihat history moderasi review

## 3.9 Help Center

* [x] Melihat tiket Buyer
* [x] Melihat tiket Seller
* [x] Mengambil/assign tiket
* [x] Membalas tiket
* [x] Mengubah status tiket
* [x] Menutup tiket
* [x] Kategori bantuan
* [x] Prioritas tiket
* [x] History percakapan bantuan

---

# 4. MODERATION & ANTI-FRAUD

Fitur ini sebaiknya menjadi bagian tersendiri karena digunakan oleh hampir semua role.

* [ ] Sistem report
* [ ] Buyer dapat report seller
* [ ] Buyer dapat report product
* [ ] Buyer dapat report review
* [ ] Seller dapat report buyer
* [ ] Seller dapat report review
* [ ] Admin dapat melihat semua report
* [ ] Admin dapat memberikan tindakan
* [ ] Warning user
* [ ] Suspend user
* [ ] Ban user
* [ ] Suspend product
* [ ] Suspend toko
* [ ] Reject promotion
* [ ] Delete content yang melanggar
* [ ] Menyimpan alasan tindakan
* [ ] Menyimpan history moderation
* [ ] Audit log aktivitas admin

---

# 5. NOTIFICATION

## Buyer

* [x] Order dibuat
* [x] Pembayaran berhasil
* [x] Order diproses
* [x] Order dikirim
* [x] Order selesai
* [ ] Voucher tersedia
* [ ] Voucher akan expired
* [x] Daily mission tersedia
* [ ] Reward diperoleh
* [x] Chat baru
* [x] Help ticket mendapatkan balasan

## Seller

* [x] Order baru
* [x] Pembayaran berhasil
* [ ] Product stok rendah
* [ ] Product habis
* [x] Review baru
* [x] Promotion approved
* [x] Promotion rejected
* [ ] Voucher digunakan
* [x] Chat baru
* [x] Help ticket mendapatkan balasan

## Admin

* [x] Promotion baru
* [ ] Report baru
* [x] Help ticket baru
* [ ] Seller/toko perlu diperiksa
* [ ] Product dilaporkan
* [ ] Review dilaporkan

---

# 6. REAL-TIME FEATURE

* [x] Real-time chat Buyer ↔ Seller
* [x] Real-time notification
* [x] Notification order
* [x] Notification promotion
* [x] Notification help ticket
* [ ] Online/offline status jika diperlukan
* [ ] Read receipt jika diperlukan

---

# 7. DASHBOARD

## Buyer Dashboard

* [x] Ringkasan order
* [x] Order belum dibayar
* [x] Order sedang diproses
* [x] Order dikirim
* [x] Wishlist
* [x] Voucher
* [x] Daily mission
* [x] Help ticket

## Seller Dashboard

* [x] Total penjualan
* [x] Total order
* [x] Product terlaris
* [x] Revenue
* [x] Expense
* [x] Profit
* [ ] Stock rendah
* [x] Order terbaru
* [x] Review terbaru
* [x] Promotion berjalan

## Admin Dashboard

* [x] Total user
* [x] Total seller
* [x] Total toko
* [x] Total product
* [x] Total order
* [x] Total revenue marketplace
* [x] Promotion pending
* [ ] Report pending
* [x] Help ticket pending
* [x] Statistik marketplace

---

# 8. URUTAN DEVELOPMENT YANG DISARANKAN

## Phase 1 — Foundation

* [x] Database architecture
* [x] Authentication
* [x] Authorization
* [x] User
* [x] Role & permission
* [x] Profile
* [x] Address
* [x] Catalog
* [x] Category

## Phase 2 — Seller Core

* [x] Store
* [x] Product
* [x] Product Image
* [x] Product Variant
* [x] Stock
* [x] HPP
* [x] Showcase

## Phase 3 — Buyer Core

* [x] Product listing
* [x] Product detail
* [x] Wishlist
* [x] Cart
* [x] Checkout
* [x] Order
* [x] Order status

## Phase 4 — Transaction

* [x] Payment
* [x] Payment verification
* [x] Order processing
* [x] Shipping
* [x] Order completion
* [ ] Refund/cancellation

## Phase 5 — Review & Communication

* [x] Rating
* [x] Review
* [x] Chat
* [x] Notification
* [x] Help Center

## Phase 6 — Seller Business Management

* [x] Income
* [x] Expense
* [x] Hutang
* [x] Piutang
* [x] Financial report
* [x] Master bahan baku
* [x] Stock movement
* [x] HPP history

## Phase 7 — Voucher & Promotion

* [x] Seller voucher
* [x] Marketplace voucher
* [ ] Voucher claim
* [x] Voucher checkout
* [x] Promotion
* [x] Promotion payment
* [x] Admin approval
* [ ] Promotion analytics

## Phase 8 — Gamification

* [x] Daily mission
* [x] Mission progress
* [x] Reward
* [ ] Voucher reward
* [x] Game
* [x] Anti-abuse reward

## Phase 9 — Admin & Moderation

* [x] User management
* [x] Seller management
* [x] Product moderation
* [ ] Review moderation
* [x] Promotion moderation
* [ ] Report system
* [x] Ban/suspend
* [ ] Audit log
* [x] Help Center

## Phase 10 — Enhancement

* [ ] Advanced search
* [ ] Recommendation system
* [ ] Advanced analytics
* [ ] Seller performance
* [ ] Promotion analytics
* [ ] Advanced notification
* [x] Real-time features
* [ ] Performance optimization
* [x] Security hardening
* [ ] Anti-fraud system

---

# 9. PRIORITAS MVP

Jika marketplace ini ingin segera dibuat sampai **bisa melakukan transaksi end-to-end**, jangan langsung mengerjakan semua fitur.

### 🔴 Wajib untuk MVP

* [x] Authentication
* [x] Role Buyer/Seller/Admin
* [x] User Profile
* [x] Address
* [x] Catalog
* [x] Category
* [x] Store
* [x] Product
* [x] Product Image
* [x] Product Variant
* [x] Stock
* [x] Cart
* [x] Checkout
* [x] Order
* [x] Payment
* [x] Order Status
* [x] Wishlist
* [x] Rating & Review
* [x] Basic Admin Management

### 🟡 Setelah MVP

* [x] Chat
* [x] Voucher
* [x] Showcase
* [x] Notification
* [x] Seller financial management
* [x] Master bahan baku
* [x] HPP management
* [x] Stock movement
* [x] Help Center

### 🟢 Advanced Feature

* [x] Daily Mission
* [ ] Game → Voucher
* [x] Paid Promotion
* [x] Promotion Approval
* [ ] Promotion Analytics
* [ ] Advanced financial report
* [ ] Recommendation
* [ ] Anti-fraud
* [ ] Advanced moderation
* [ ] Real-time analytics

---

# 10. CORE BUSINESS FLOW

Marketplace sebaiknya dianggap selesai secara fundamental ketika alur berikut sudah berjalan:

### Buyer

* [x] Register/Login
* [x] Melihat catalog
* [x] Melihat product
* [x] Memilih variant
* [x] Menambahkan ke cart
* [x] Checkout
* [x] Membayar
* [x] Menunggu seller memproses
* [x] Menerima barang
* [x] Menyelesaikan order
* [x] Memberikan rating & review

### Seller

* [x] Register/Login
* [x] Membuat toko
* [x] Membuat product
* [x] Menentukan harga
* [x] Menentukan HPP
* [x] Mengatur stock
* [x] Menerima order
* [x] Memproses order
* [x] Mengirim order
* [x] Mendapatkan pendapatan
* [x] Melihat review

### Admin

* [x] Mengelola catalog
* [x] Mengelola user
* [x] Mengelola seller
* [x] Memoderasi product
* [ ] Memoderasi review
* [x] Mengelola voucher
* [x] Mengelola promotion
* [ ] Menangani report
* [x] Menangani help ticket
* [x] Melakukan suspend/ban jika diperlukan

---

## Prinsip Pembagian Modul

Supaya arsitektur aplikasi tidak ikut berantakan, fitur-fitur di atas sebaiknya nanti dipisahkan menjadi domain/module seperti:

**Identity**
→ User, Auth, Role, Permission, Profile, Address

**Catalog**
→ CatalogGroup, Category, Product, Attribute, Variant

**Store**
→ Store, Showcase, Store Decoration

**Inventory**
→ Stock, StockMovement, RawMaterial, HPP

**Cart**
→ Cart, CartItem, Wishlist

**Order**
→ Order, OrderItem, Payment, Shipment

**Promotion**
→ Voucher, Campaign, Promotion

**Review**
→ Rating, Review, Report

**Communication**
→ Chat, Conversation, Message, Notification

**Gamification**
→ Mission, Game, Reward

**Finance**
→ Income, Expense, Debt, Receivable, FinancialReport

**Support**
→ HelpTicket, HelpMessage

**Moderation**
→ Report, ModerationAction, AuditLog

Dengan pembagian ini, **Buyer/Seller/Admin bukan menjadi domain utama**, melainkan role yang mempunyai akses berbeda terhadap domain-domain tersebut. Ini akan jauh lebih cocok dengan pendekatan DDD yang sedang kamu gunakan di backend Laravel.
