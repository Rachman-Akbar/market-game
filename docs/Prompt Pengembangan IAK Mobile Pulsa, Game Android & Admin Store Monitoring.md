# MARKETPLACE — IAK / MOBILE PULSA, GAME ANDROID & ADMIN STORE MONITORING

Kamu bertindak sebagai:

- Senior Software Architect
- Senior Laravel Backend Engineer
- Senior Next.js Frontend Engineer
- Senior Android/Kotlin Engineer
- Database Engineer
- PPOB / Digital Payment Integration Engineer
- Finance System Engineer
- Performance Engineer
- UX/UI Architect
- QA Engineer

Project terdiri dari:

```text
/
├── docs/
│   ├── roadmap.md
│   └── roadmap-audit.md
│
├── backend/
│   └── market-api/
│
├── frontend/
│   └── market-frontend/
│
└── android/
    └── games/
```

Saya ingin mengembangkan marketplace dengan menambahkan:

1. PPOB menggunakan **IAK / Mobile Pulsa**.
2. Pulsa.
3. Token listrik.
4. Tagihan listrik.
5. Internet/WiFi.
6. Produk digital lainnya yang didukung IAK/Mobile Pulsa.
7. Sistem harga jual.
8. Margin keuntungan.
9. Komisi.
10. Biaya/admin fee.
11. Provider cost.
12. Finance/ledger.
13. Dashboard Admin.
14. Dashboard Seller.
15. Game Arithmetic Kilat.
16. Game Sudoku.
17. Reward game.
18. Admin Store Monitoring.
19. Admin Store Context.
20. Optimasi performa dan cache.

---

# 1. WAJIB AUDIT SEBELUM CODING

Sebelum melakukan perubahan:

1. Baca `docs/roadmap.md`.
2. Baca `docs/roadmap-audit.md` jika tersedia.
3. Audit seluruh backend `market-api`.
4. Audit seluruh frontend `market-frontend`.
5. Audit Android `games`.
6. Audit database.
7. Cari implementasi:
   - authentication
   - user
   - wallet
   - payment
   - order
   - finance
   - commission
   - voucher
   - notification
   - game
   - reward
   - store
   - admin
   - seller
   - product
   - transaction

Jika fungsi yang sama sudah tersedia:

> gunakan dan sempurnakan implementasi existing. Jangan membuat duplicate service/controller/repository.

---

# 2. DATABASE SAFETY

Database sudah memiliki data testing.

### DILARANG:

```text
migrate:fresh
migrate:refresh
TRUNCATE
DROP DATABASE
delete seluruh data
reset database
destructive seeder
```

### BOLEH:

- migration baru
- tabel baru
- kolom baru
- index
- foreign key
- unique constraint
- safe data migration
- backfill data jika aman

**Jangan menghapus data existing.**

---

# 3. IAK / MOBILE PULSA

Gunakan **IAK / Mobile Pulsa** sebagai provider PPOB.

Sebelum melakukan implementasi:

> Periksa terlebih dahulu implementasi API/SDK yang sudah ada di project dan dokumentasi resmi IAK / Mobile Pulsa yang sesuai dengan layanan yang digunakan.

Jangan mengarang:

- endpoint
- parameter
- signature
- response
- callback
- kode produk
- format inquiry
- format transaksi

Jika ada perbedaan antara API IAK dan implementasi existing:

> sesuaikan berdasarkan dokumentasi provider yang benar.

---

# 4. ENVIRONMENT CONFIGURATION

Credential provider **tidak boleh hardcode**.

Gunakan `.env`.

Contoh struktur:

```env
IAK_USERNAME=
IAK_API_KEY=
IAK_BASE_URL=
IAK_CALLBACK_URL=
```

Jika API IAK menggunakan nama credential atau mekanisme authentication yang berbeda:

> gunakan format sesuai dokumentasi resmi dan architecture project.

Pastikan:

- credential tidak masuk Git
- credential tidak muncul di frontend
- credential tidak masuk response API
- credential tidak masuk log
- credential tidak ditulis hardcode

---

# 5. PPOB PRODUCT

Buat sistem produk digital yang extensible.

Minimal dukung:

### Pulsa

- [ ] Pulsa semua operator yang tersedia
- [ ] Nominal pulsa
- [ ] Provider product code
- [ ] Provider cost
- [ ] Harga jual
- [ ] Margin
- [ ] Commission
- [ ] Admin fee
- [ ] Status product

### Token Listrik

- [ ] PLN Token
- [ ] Nomor meter / ID pelanggan
- [ ] Nominal
- [ ] Provider product
- [ ] Provider cost
- [ ] Margin
- [ ] Admin fee
- [ ] Selling price

### Tagihan Listrik

- [ ] PLN Postpaid
- [ ] Customer ID
- [ ] Inquiry
- [ ] Bill amount
- [ ] Admin fee
- [ ] Provider cost
- [ ] Margin
- [ ] Payment

### Internet / WiFi

Jika tersedia pada produk/provider IAK:

- [ ] Customer ID
- [ ] Inquiry
- [ ] Bill
- [ ] Admin fee
- [ ] Provider cost
- [ ] Margin
- [ ] Payment
- [ ] Status

Jangan membuat asumsi bahwa semua produk tersedia pada akun IAK.

Gunakan daftar produk yang benar-benar tersedia dari provider.

---

# 6. DIGITAL PRODUCT ARCHITECTURE

Gunakan model/domain yang dapat menangani berbagai produk PPOB.

Contoh:

```text
DigitalProduct
├── category
├── provider
├── provider_product_code
├── name
├── operator/brand
├── provider_price
├── selling_price
├── admin_fee
├── margin
├── commission
├── status
└── metadata
```

Jika database existing memiliki struktur Product yang dapat digunakan:

> jangan membuat DigitalProduct terpisah tanpa alasan yang kuat.

Evaluasi terlebih dahulu apakah PPOB dapat menjadi product type yang berbeda dari physical product.

---

# 7. PRICING ENGINE

Buat satu pricing engine.

Jangan menyebarkan perhitungan harga ke banyak controller.

Contoh konsep:

```text
Provider Cost
+
Provider/Admin Fee
+
Marketplace Margin
-
Commission
=
Net Marketplace Result
```

Namun formula final harus mengikuti business rule dan struktur transaksi yang digunakan.

Jangan mengarang formula finansial.

Admin harus dapat melihat:

```text
Provider Cost
Admin Fee
Commission
Margin
Selling Price
Net Profit
```

---

# 8. ADMIN PPOB MANAGEMENT

Admin dapat mengelola:

- [ ] Product PPOB
- [ ] Status active/inactive
- [ ] Provider
- [ ] Provider product code
- [ ] Provider cost
- [ ] Selling price
- [ ] Margin
- [ ] Commission
- [ ] Admin fee
- [ ] Product availability
- [ ] Pricing rule

Gunakan konfigurasi yang terpusat.

Contoh hierarchy:

```text
Global Rule
 ↓
PPOB Category Rule
 ↓
Operator Rule
 ↓
Product Rule
 ↓
Specific Override
```

Jika architecture existing lebih sederhana, gunakan struktur yang lebih sederhana.

Jangan membuat konfigurasi berlebihan tanpa kebutuhan.

---

# 9. PROVIDER COST

Admin harus dapat mengetahui harga dari IAK/Mobile Pulsa.

Contoh:

| Komponen | Nilai |
|---|---:|
| Harga Provider | Rp10.000 |
| Admin Fee | Rp500 |
| Margin | Rp1.000 |
| Commission | Rp200 |
| Harga Jual | Rp11.300 |
| Net Profit | Rp... |

Angka harus berasal dari calculation engine.

Jangan hardcode angka.

---

# 10. PPOB TRANSACTION FLOW

Gunakan flow seperti:

```text
User
 ↓
Select PPOB Product
 ↓
Input Customer Number
 ↓
Inquiry jika diperlukan
 ↓
Display Customer/Bill
 ↓
Display Price
 ↓
Confirm
 ↓
Create Local Transaction
 ↓
Payment
 ↓
Submit to IAK/Mobile Pulsa
 ↓
Provider Response
 ↓
Callback / Status Check
 ↓
Update Transaction
 ↓
Update Finance
 ↓
Update Commission/Margin
 ↓
Notification
```

Backend menjadi source of truth.

Frontend tidak boleh menentukan transaksi sebagai `SUCCESS` hanya berdasarkan response client.

---

# 11. TRANSACTION STATUS

Gunakan state yang konsisten.

Contoh:

```text
PENDING
PROCESSING
SUCCESS
FAILED
EXPIRED
REFUNDED
```

Sesuaikan dengan response/status IAK.

Jangan membuat banyak status yang memiliki arti sama.

---

# 12. IAK CALLBACK

Jika layanan IAK yang digunakan menyediakan callback:

Implementasikan callback dengan benar.

Pastikan:

- authentication/signature sesuai provider
- validasi request
- idempotency
- duplicate prevention
- transaction locking jika diperlukan
- finance update
- commission update
- notification

Jika callback dikirim dua kali:

> transaksi hanya boleh diproses satu kali.

---

# 13. STATUS CHECK

Jika transaksi IAK membutuhkan pengecekan status:

gunakan mekanisme status inquiry/check yang sesuai dokumentasi provider.

Jangan:

```text
request setiap 1 detik tanpa batas
```

Gunakan:

- exponential backoff
- queue
- retry limit
- timeout
- status transition

Jika transaksi sudah final:

> hentikan retry.

---

# 14. IDEMPOTENCY

PPOB sangat sensitif terhadap transaksi duplicate.

Cegah:

```text
User klik dua kali
 ↓
2 transaksi IAK
```

Gunakan:

- unique transaction reference
- idempotency key
- database constraint
- transaction lock
- state validation

Test:

```text
double click
refresh
retry
network timeout
duplicate callback
duplicate request
```

---

# 15. PPOB FINANCE

Setiap transaksi harus masuk finance.

Minimal:

```text
Revenue
Provider Cost
Admin Fee
Commission
Margin
Net Profit
```

Admin dapat melihat:

- total transaksi
- successful transaction
- failed transaction
- provider cost
- revenue
- commission
- admin fee
- margin
- net profit

---

# 16. FINANCE LEDGER

Gunakan reference:

```text
source_type
source_id
transaction_type
amount
```

Contoh:

```text
source_type = ppob_transaction
source_id = 12345
```

Jangan membuat finance record tanpa referensi transaksi jika transaction source memang tersedia.

---

# 17. DASHBOARD ADMIN

Admin dashboard **tidak boleh menyerupai dashboard Seller secara penuh**.

Admin membutuhkan overview marketplace.

### Marketplace

- Total Store
- Active Store
- Total User
- Total Product
- Total Order
- Total PPOB Transaction

### Sales

- Today's GMV
- Monthly GMV
- Completed Orders

### PPOB

- PPOB Today
- PPOB Revenue
- Provider Cost
- Commission
- Admin Fee
- Profit

### Store

- Top Selling Store
- Top Revenue Store
- Store Growth
- Store yang membutuhkan perhatian

### Monitoring

- Failed transaction
- Pending transaction
- Reports
- Suspicious activity
- Pending approval

---

# 18. TOP STORE

Admin dapat melihat:

| Store | Sales | Orders | Rating | Status |
|---|---:|---:|---:|---|
| Store A | ... | ... | ... | Active |
| Store B | ... | ... | ... | Active |

Admin dapat memilih:

```text
Store A
```

kemudian masuk ke:

```text
Store Context
```

---

# 19. ADMIN STORE CONTEXT

Jangan menampilkan seluruh data marketplace setelah Admin masuk ke halaman store.

Gunakan:

```text
Admin
 ↓
Select Store
 ↓
Store Context
```

Contoh:

```text
/admin
/admin/stores
/admin/stores/{storeId}
/admin/stores/{storeId}/dashboard
/admin/stores/{storeId}/products
/admin/stores/{storeId}/orders
/admin/stores/{storeId}/stock
/admin/stores/{storeId}/finance
```

Sesuaikan routing dengan architecture existing.

---

# 20. STORE-SCOPED DATA

Jika Admin memilih:

```text
Store A
```

semua data berikut harus scoped:

- Product
- Variant
- Stock
- Showcase
- Voucher
- Promotion
- Order
- Review
- Finance
- HPP
- Stock movement
- Raw material

Backend harus melakukan filtering.

**Jangan hanya melakukan filtering di frontend.**

Contoh:

```text
GET /admin/stores/{storeId}/orders
```

harus benar-benar hanya mengembalikan order milik store tersebut.

---

# 21. ADMIN VS SELLER DESIGN

### Seller

Seller langsung berada di:

```text
My Store
```

dan memiliki:

- Dashboard
- Product
- Order
- Stock
- Finance
- Voucher
- Promotion
- Showcase
- Review
- Store Decoration

### Admin

Admin berada di:

```text
Marketplace Overview
```

kemudian:

```text
Store Monitoring
 ↓
Select Store
 ↓
Store Context
```

Admin memiliki fitur tambahan:

- User management
- Store moderation
- Catalog
- Promotion approval
- Review moderation
- Reports
- PPOB management
- Game management
- System settings

Jangan membuat Admin hanya sebagai Seller dengan permission tambahan.

---

# 22. ADMIN PERFORMANCE

Ketika Admin membuka `/admin`:

**Jangan langsung load seluruh transaction table.**

Load:

```text
Summary
Charts
Top Stores
Alerts
```

Jika Admin ingin melihat transaksi:

```text
Transactions
 ↓
Filter
 ↓
Pagination
```

Jika Admin memilih toko:

```text
Store Context
 ↓
Store-scoped query
```

---

# 23. CACHE

Gunakan cache untuk data yang cocok di-cache:

- PPOB product catalog
- provider product
- category
- operator
- store summary
- dashboard summary
- catalog
- configuration

Jangan cache secara sembarangan:

- payment status
- transaction final state
- finance ledger
- sensitive data

Gunakan:

```text
Cache
+
Event-driven invalidation
+
On-demand refresh
```

---

# 24. FRONTEND PERFORMANCE

Automatic fetch hanya untuk:

```text
AUTH
NOTIFICATION
REAL-TIME EVENT
```

Data lainnya:

```text
ON DEMAND
```

Optimalkan:

- TanStack Query
- queryKey
- staleTime
- gcTime
- cache
- pagination
- infinite scroll jika memang diperlukan
- lazy loading

Jangan menggunakan polling setiap detik tanpa alasan.

---

# 25. USEMEMO

Gunakan `useMemo` hanya jika memang ada expensive calculation.

Contoh:

- dashboard aggregation
- filtering data besar
- sorting data besar
- chart transformation
- derived data kompleks

Jangan:

```text
useMemo(() => simpleValue, [])
```

di seluruh aplikasi hanya untuk "optimasi".

Jika tidak memberikan manfaat:

> jangan gunakan `useMemo`.

---

# 26. ANDROID — ARITHMETIC KILAT

Tambahkan game:

# Arithmetic Kilat

User diberikan pola angka dan harus menentukan:

- angka berikutnya
- angka sebelumnya
- jumlah
- selisih
- hasil operasi
- angka yang hilang

Contoh:

```text
2
4
6
8
?
```

atau:

```text
3
6
12
24
?
```

atau:

```text
10
8
6
4
?
```

---

# 27. ARITHMETIC DIFFICULTY

Gunakan:

```text
EASY
MEDIUM
HARD
```

### Easy

- angka kecil
- pola sederhana
- waktu lebih longgar

### Medium

- angka lebih besar
- pola kombinasi
- waktu lebih pendek

### Hard

- alternating pattern
- mixed operation
- pola kompleks
- time pressure

Soal harus selalu memiliki jawaban yang valid dan tidak ambigu.

---

# 28. ARITHMETIC GAME DATA

Setiap soal minimal memiliki:

```text
question
pattern_type
numbers
answer
difficulty
time_limit
```

Jangan hanya mengirim jawaban dari Android tanpa validasi backend jika game memberikan reward.

---

# 29. SUDOKU

Tambahkan Sudoku dengan:

```text
EASY
MEDIUM
HARD
```

Puzzle harus:

- valid
- memiliki solusi
- unique solution jika diperlukan
- difficulty sesuai
- dapat divalidasi

---

# 30. SUDOKU FLOW

```text
Select Difficulty
 ↓
Generate Puzzle
 ↓
Play
 ↓
Validate
 ↓
Complete
 ↓
Score
 ↓
Reward
```

Simpan:

```text
game_session
difficulty
start_time
completion_time
score
result
```

---

# 31. GAME REWARD

Jika Arithmetic Kilat atau Sudoku memberikan:

- voucher
- coin
- point
- reward

jangan mempercayai reward dari Android.

Flow:

```text
Game Complete
 ↓
Backend Validation
 ↓
Calculate Reward
 ↓
Create Reward Transaction
 ↓
User Reward
```

Gunakan unique constraint agar satu game session tidak dapat memberikan reward berkali-kali.

---

# 32. ANTI-CHEAT

Cegah:

- duplicate submission
- replay request
- impossible score
- impossible completion time
- reward farming
- manipulated game result

Jika memungkinkan:

> backend menjadi validator hasil game.

---

# 33. ADMIN GAME MANAGEMENT

Admin dapat mengelola:

### Arithmetic

- difficulty
- enabled/disabled
- time limit
- score
- reward

### Sudoku

- difficulty
- enabled/disabled
- reward

Jangan membuat Admin dapat merusak algorithm generator melalui konfigurasi yang tidak tervalidasi.

---

# 34. NOTIFICATION

PPOB:

- transaction created
- processing
- success
- failed
- refund

Game:

- game completed
- reward received

Admin:

- provider error
- failed transaction
- suspicious transaction

---

# 35. ERROR HANDLING

Jangan tampilkan error mentah kepada user.

Jangan:

```text
SQLSTATE...
Stack trace...
cURL...
API key...
```

Gunakan:

```text
Transaction sedang diproses.
Silakan tunggu beberapa saat.
```

Detail teknis masuk log.

---

# 36. SECURITY

Audit:

- API authentication
- authorization
- IDOR
- admin permission
- seller permission
- provider credentials
- webhook
- callback
- transaction
- finance
- reward
- file upload

Seller hanya dapat mengakses data tokonya.

Admin dapat mengakses sesuai permission.

Buyer hanya dapat mengakses data miliknya.

---

# 37. TESTING IAK / MOBILE PULSA

### Pulsa

- [ ] valid phone number
- [ ] invalid phone number
- [ ] product active
- [ ] product inactive
- [ ] successful transaction
- [ ] failed transaction
- [ ] duplicate transaction

### Token PLN

- [ ] valid customer ID
- [ ] invalid customer ID
- [ ] inquiry
- [ ] purchase
- [ ] success
- [ ] failure

### Electricity Bill

- [ ] inquiry
- [ ] bill display
- [ ] payment
- [ ] failure
- [ ] refund

### Internet/WiFi

- [ ] inquiry jika tersedia
- [ ] customer validation
- [ ] payment
- [ ] success
- [ ] failure

---

# 38. TESTING FINANCE

Pastikan:

```text
PPOB Transaction
 ↓
Provider Cost
 ↓
Revenue
 ↓
Commission
 ↓
Admin Fee
 ↓
Margin
 ↓
Finance
```

Angka harus sama pada:

- transaction
- finance
- admin dashboard
- report

---

# 39. TESTING ADMIN STORE

Test:

```text
Admin Login
 ↓
Admin Overview
 ↓
Top Stores
 ↓
Select Store
 ↓
Store Dashboard
 ↓
Products
 ↓
Orders
 ↓
Stock
 ↓
Finance
```

Kemudian:

```text
Switch Store
```

Pastikan semua data berubah sesuai store.

---

# 40. TESTING GAME

### Arithmetic

- [ ] Easy
- [ ] Medium
- [ ] Hard
- [ ] correct answer
- [ ] wrong answer
- [ ] timer
- [ ] score
- [ ] completion
- [ ] reward
- [ ] duplicate reward

### Sudoku

- [ ] Easy
- [ ] Medium
- [ ] Hard
- [ ] valid puzzle
- [ ] invalid move
- [ ] completion
- [ ] score
- [ ] reward
- [ ] duplicate reward

---

# 41. TESTING DUPLICATION

Test:

```text
Double Click
Rapid Click
Refresh
Retry
Network Timeout
Duplicate Callback
Duplicate Webhook
Duplicate Game Submission
```

Tidak boleh menghasilkan:

- duplicate transaction
- duplicate order
- duplicate finance
- duplicate commission
- duplicate reward
- duplicate notification

---

# 42. DATABASE MIGRATION

Jika membutuhkan tabel baru:

```text
PPOB Products
PPOB Transactions
PPOB Transaction Logs
PPOB Pricing Rules
PPOB Finance Entries
Game Sessions
Game Questions
Game Results
Game Rewards
```

Tetapi:

> jangan otomatis membuat semua tabel tersebut.

Audit database existing terlebih dahulu dan gunakan tabel existing jika sudah memenuhi kebutuhan.

---

# 43. DOCUMENTATION

Update:

```text
docs/roadmap-audit.md
```

Tambahkan:

```text
IAK / Mobile Pulsa
PPOB
Pricing
Margin
Commission
Finance
Arithmetic Kilat
Sudoku
Reward
Admin Overview
Store Context
Performance
Cache
Security
Testing
```

---

# 44. FINAL REPORT

Setelah implementasi selesai, berikan summary:

## Fitur Baru

- IAK / Mobile Pulsa
- Pulsa
- Token
- Listrik
- WiFi
- Finance
- Margin
- Commission
- Arithmetic
- Sudoku
- Reward
- Admin Store Context

## Bug Fixed

Jelaskan bug yang ditemukan dan diperbaiki.

## Database

Jelaskan:

- tabel
- kolom
- index
- foreign key
- migration

Pastikan:

> Tidak ada data existing yang dihapus.

## IAK

Jelaskan:

- authentication
- product
- transaction
- inquiry
- callback
- status check
- retry
- idempotency

## Finance

Jelaskan:

- provider cost
- revenue
- admin fee
- commission
- margin
- profit

## Android

Jelaskan:

- Arithmetic Kilat
- Easy
- Medium
- Hard
- Sudoku
- Easy
- Medium
- Hard
- score
- reward
- anti-cheat

## Admin

Jelaskan:

```text
Admin Overview
 ↓
Top Store
 ↓
Select Store
 ↓
Store Context
 ↓
Store Management
```

## Performance

Jelaskan:

- cache
- query optimization
- pagination
- lazy loading
- reduced requests
- memoization
- real-time strategy

---

# 45. FINAL ACCEPTANCE CRITERIA

Fitur dianggap selesai jika:

- [ ] IAK / Mobile Pulsa terintegrasi
- [ ] Pulsa bekerja
- [ ] Token listrik bekerja
- [ ] Tagihan listrik bekerja jika tersedia pada layanan IAK
- [ ] Internet/WiFi bekerja jika tersedia pada layanan IAK
- [ ] Provider cost dapat dilihat
- [ ] Admin dapat mengatur margin
- [ ] Admin dapat mengatur commission
- [ ] Admin dapat melihat admin fee
- [ ] Admin dapat melihat profit
- [ ] PPOB transaction idempotent
- [ ] Callback aman
- [ ] Finance konsisten
- [ ] Arithmetic Kilat tersedia
- [ ] Easy/Medium/Hard tersedia
- [ ] Sudoku tersedia
- [ ] Easy/Medium/Hard tersedia
- [ ] Reward aman
- [ ] Anti-cheat diterapkan
- [ ] Admin Overview tersedia
- [ ] Top Store tersedia
- [ ] Store Selection tersedia
- [ ] Store Context tersedia
- [ ] Store-scoped data benar
- [ ] Admin UI berbeda dengan Seller UI
- [ ] Dashboard efisien
- [ ] Cache diterapkan
- [ ] Tidak ada unnecessary polling
- [ ] Tidak ada duplicate transaction
- [ ] Tidak ada duplicate finance entry
- [ ] Tidak ada duplicate reward
- [ ] Tidak ada duplicate callback processing
- [ ] Database existing aman
- [ ] Tidak ada critical security issue
- [ ] Build berhasil
- [ ] Test berhasil

---

# 46. PRINSIP AKHIR

Prioritas pengembangan:

```text
DATA INTEGRITY
      ↓
SECURITY
      ↓
FINANCIAL ACCURACY
      ↓
CORRECTNESS
      ↓
PERFORMANCE
      ↓
UX
      ↓
MAINTAINABILITY
```

Jangan membuat implementasi berdasarkan asumsi tentang API IAK/Mobile Pulsa.

Gunakan dokumentasi/provider contract yang benar.

Jangan menganggap response `success` dari provider sebagai transaksi final sebelum status dan business rule tervalidasi.

Jangan membuat duplicate architecture.

Jangan menghapus data existing.

Jangan menggunakan polling berlebihan.

Jangan menggunakan `useMemo` secara berlebihan.

Jangan mempercayai score/reward yang dikirim client.

Dan pertahankan konsep:

```text
ADMIN
 ↓
Marketplace Overview
 ↓
Monitor Stores
 ↓
Select Store
 ↓
Store Context
 ↓
Manage / Investigate Store
```

sedangkan:

```text
SELLER
 ↓
My Store
 ↓
Manage My Business
```

Tujuan akhirnya adalah marketplace yang memiliki **PPOB IAK/Mobile Pulsa yang aman dan dapat diaudit, finance yang akurat, game Android yang aman dari abuse, serta Admin Panel yang ringan karena menggunakan konsep Store Context daripada memuat seluruh data semua toko sekaligus.**