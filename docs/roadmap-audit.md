# Marketplace Roadmap Audit

---

## 1. Executive Summary

| Metric | Value |
|---|---|
| **Roadmap Completion** | ~52% |
| **Backend Complete** | ~78% |
| **Frontend Complete** | ~70% |
| **Android Complete** | ~15% (game only, zero marketplace integration) |
| **Integration Consistent** | ~45% |
| **Critical Bugs (P0)** | 4 |
| **High Bugs (P1)** | 7 |
| **Medium Bugs (P2)** | 11 |
| **Low Bugs (P3)** | 8 |
| **Security Critical** | 3 |
| **Security High** | 4 |
| **Security Medium** | 5 |

**Overall Status: NOT READY**

Backend has strong architectural foundations with DDD, comprehensive spreadsheet support, and solid business logic. Frontend has a well-structured React app with full CRUD coverage. However, critical security vulnerabilities (IDOR on user profiles, unauthenticated voucher endpoint), missing API routes (seller product costing), broken Android-marketplace integration, and several unfinished features make this system unsafe for production or even beta use.

---

## 2. Architecture Overview

### Backend (market-api)
- **Framework:** Laravel 13, PHP 8.3+
- **Architecture:** Domain-Driven Design (DDD) with Clean Architecture
- **Domains:** Identity, Catalog, Seller, Order, Communication, Support, Engagement, Admin, Shared, Template
- **Auth:** Laravel Sanctum + Firebase Auth
- **Payment:** Midtrans (Snap + Webhook)
- **Realtime:** Laravel Reverb (WebSocket)
- **Shipping:** RajaOngkir / Komerce
- **Spreadsheet:** PhpSpreadsheet (16 modules)
- **Permissions:** Spatie Laravel Permission
- **Database:** 44 tables, UUID primary keys on users

### Frontend (market-frontend)
- **Framework:** React 18 + Vite 7
- **State:** TanStack Query + React Context
- **Styling:** Tailwind CSS v4
- **Realtime:** Laravel Echo + Pusher.js/Reverb
- **Maps:** Leaflet + OpenStreetMap/Nominatim
- **Language:** JavaScript (JSX) — no TypeScript strict mode
- **Architecture:** Feature-based folder structure

### Android (market-game)
- **Framework:** Kotlin, Jetpack Compose
- **Architecture:** MVVM (ViewModel + Repository)
- **API:** Retrofit + Moshi
- **Network:** Dynamic base URL (emulator/local/production)
- **Purpose:** SDG Environmental mini-game app (Quiz, Trash Sort, Myth/Fact, SDG Match, Clean River)
- **Marketplace Integration:** Minimal — only auth, products listing, cart, and orders exist but are barely functional

---

## 3. Feature Matrix

| Feature | Roadmap | Backend | Frontend | Android | Integration | Status |
|---|---|---|---|---|---|---|
| Register | ✅ | ✅ | ✅ | ✅ | ✅ | COMPLETE |
| Login | ✅ | ✅ | ✅ | ✅ | ✅ | COMPLETE |
| Logout | ✅ | ✅ | ✅ | ✅ | ✅ | COMPLETE |
| Forgot Password | ✅ | ✅ | ✅ | ❌ | ⚠️ | PARTIAL |
| Google Login | ✅ | ✅ | ✅ | ❌ | ❌ | PARTIAL |
| Email Verification | ✅ | ✅ | ❌ | ❌ | ❌ | PARTIAL |
| Role Switch | ✅ | ✅ | ✅ | ❌ | ❌ | PARTIAL |
| Ban/Suspend User | ✅ | ✅ | ✅ (admin) | ❌ | ❌ | PARTIAL |
| User Profile CRUD | ✅ | ✅ | ✅ | ✅ (basic) | ⚠️ | PARTIAL |
| Upload Avatar | ✅ | ✅ | ❌ | ❌ | ❌ | MISSING |
| Address CRUD | ✅ | ✅ | ✅ | ❌ | ❌ | PARTIAL |
| Catalog Group CRUD | ✅ | ✅ | ✅ (admin) | ❌ | ❌ | COMPLETE |
| Category CRUD (L1-L3) | ✅ | ✅ | ✅ (admin) | ❌ | ❌ | COMPLETE |
| Product Discovery | ✅ | ✅ | ✅ | ✅ | ✅ | COMPLETE |
| Product Detail | ✅ | ✅ | ✅ | ✅ | ✅ | COMPLETE |
| Product Search/Filter/Sort | ✅ | ✅ | ✅ | ❌ | ❌ | PARTIAL |
| Product Image | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Product Variant | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Wishlist | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Cart | ✅ | ✅ | ✅ | ✅ (stub) | ⚠️ | PARTIAL |
| Checkout | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Order Creation | ✅ | ✅ | ✅ | ✅ (stub) | ⚠️ | PARTIAL |
| Payment (Midtrans) | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Order Status | ✅ | ✅ | ✅ (basic) | ❌ | ⚠️ | PARTIAL |
| Order History | ✅ | ✅ | ✅ (redirect) | ❌ | ⚠️ | PARTIAL |
| Order Detail Page | ✅ | ✅ | ⚠️ (redirect) | ❌ | ❌ | BROKEN |
| Cancel Order | ✅ | ✅ | ❌ | ❌ | ❌ | PARTIAL |
| Confirm Receipt | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Review & Rating | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Chat (Buyer↔Seller) | ✅ | ✅ | ✅ | ❌ | ❌ | PARTIAL |
| Chat (Admin Ban Msg) | ✅ | ❌ | ❌ | ❌ | ❌ | MISSING |
| Voucher (View/Claim) | ✅ | ✅ | ⚠️ | ❌ | ❌ | PARTIAL |
| Voucher (Use at Checkout) | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Seller Voucher CRUD | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Admin Voucher CRUD | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Store Management | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Seller Onboarding | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Showcase / Etalase | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Product Costing (HPP) | ✅ | ✅ | ✅ | ❌ | ❌ | BROKEN |
| Raw Materials | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Stock Management | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Stock Movement | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Finance (Income/Expense) | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Hutang & Piutang | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Financial Summary | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Promotion (Campaign) | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Promotion Payment | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Promotion Approval | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Banner Management | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Help Center (Tickets) | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Daily Mission | ✅ | ✅ | ✅ (admin) | ❌ | ❌ | PARTIAL |
| Daily Mission (User) | ✅ | ✅ | ✅ (buyer) | ❌ | ❌ | PARTIAL |
| Daily Mission (Android) | ✅ | ❌ | ❌ | ⚠️ (dummy) | ❌ | BROKEN |
| Game→Mission Progress | ✅ | ❌ | N/A | ❌ | ❌ | MISSING |
| Export/Import Excel | ✅ | ✅ | ✅ | ❌ | ✅ | COMPLETE |
| Dashboard (Buyer) | ✅ | ✅ | ✅ | ❌ | ⚠️ | PARTIAL |
| Dashboard (Seller) | ✅ | ✅ | ✅ | ❌ | ⚠️ | PARTIAL |
| Dashboard (Admin) | ✅ | ✅ | ✅ | ❌ | ⚠️ | PARTIAL |
| Admin User Management | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Admin Store Management | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Admin Product Moderation | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Admin Review Moderation | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Admin Notification | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Real-time Chat | ✅ | ✅ | ✅ | ❌ | ❌ | COMPLETE |
| Real-time Notification | ✅ | ✅ | ✅ (admin) | ❌ | ❌ | PARTIAL |
| Report System | ✅ | ❌ | ❌ | ❌ | ❌ | MISSING |
| Anti-fraud | ✅ | ❌ | ❌ | ❌ | ❌ | MISSING |
| Audit Log | ✅ | ⚠️ | ❌ | ❌ | ❌ | MISSING |

### Status Summary

| Status | Count |
|---|---|
| COMPLETE | 38 |
| PARTIAL | 20 |
| MISSING | 12 |
| BROKEN | 4 |
| INCONSISTENT | 1 |

---

## 4. Backend Audit

### 4.1 Architecture

**Strengths:**
- Well-structured DDD with 10+ domains
- Clean separation: Domain → Application → Infrastructure → Presentation
- Proper use of Repository pattern (28 repositories)
- Use Case pattern (58 use cases)
- API Resources for response formatting (36 resources)
- Form Requests for validation (33 request classes)
- Policies for authorization (9 policies)
- Events for realtime (2 events: MessageSent, AdminNotificationCreated)
- Shared components (ResolvesActiveRole, ResolvesSellerStoreContext, TracksUserChanges)

**Weaknesses:**
- No test files found anywhere (phpunit.xml exists but no test directory)
- No Observers found
- No Jobs/Queues for heavy operations
- No Notification classes (only Events for broadcasting)
- Only 2 events total — limited event-driven architecture

### 4.2 Database (14 migrations)

**44 tables total.** Key tables:
- `users` (UUID PK, soft deletes, banned_at)
- `stores`, `store_details`
- `catalog_groups`, `categories` (adjacency list)
- `products`, `product_variants`, `product_images`, `product_attributes`, `product_attribute_values`, `product_variant_values`
- `carts`, `cart_items`
- `orders`, `sub_orders`, `order_items`
- `payments`
- `vouchers`, `user_vouchers`
- `promotions`, `promotion_payments`
- `product_reviews`
- `raw_materials`, `raw_material_stock_movements`, `raw_material_cost_histories`
- `product_costings`, `product_costing_impacts`, `product_materials`
- `stock_movements`
- `financial_transactions`, `financial_payment_histories`
- `conversations`, `conversation_participants`, `chat_messages`, `chat_message_reads`
- `support_tickets`, `support_ticket_messages`
- `missions`, `mission_user_progress`
- `banners`
- `wishlists`, `wishlist_items`
- `showcases`, `showcase_products`
- `admin_notifications`

**Database Issues:**
- `product_costings` table has no foreign key to `products` (implicit by convention only)
- `mission_user_progress` has no unique constraint on `(mission_id, user_id)` — could create duplicates
- `chat_message_reads` has no unique constraint on `(message_id, user_id)`
- `user_vouchers` has `insertOrIgnore` usage but no unique index visible
- `raw_material_cost_histories` lacks a foreign key to `raw_materials`

### 4.3 API Endpoints

**40+ controllers, 80+ endpoints.** Key routes:

| Domain | Endpoints | Auth | Issues |
|---|---|---|---|
| Identity/Auth | 8 | Varies | `verified.email` missing on some |
| Identity/User | 5 | Mixed | **IDOR on GET/PUT /users/{id}** |
| Catalog/Products | 8+ | Public + Auth | OK |
| Catalog/Categories | 6+ | Public + Admin | OK |
| Catalog/CatalogGroups | 6+ | Public + Admin | OK |
| Catalog/Banners | 5+ | Public + Auth | OK |
| Catalog/Promotions | 8+ | Public + Auth | OK |
| Catalog/Spreadsheets | 5 | Auth (seller/admin) | OK |
| Seller/Stores | 6+ | Public + Auth | OK |
| Seller/Finance | 5 | Auth | OK |
| Seller/Stock | 2 | Auth | OK |
| Seller/Inventory | 5 | Auth | OK |
| Seller/Showcase | 5 | Auth | OK |
| Seller/Customers | 1 | Auth | OK |
| Order/Cart | 5 | Auth | Missing `verified.email` |
| Order/Addresses | 4 | Auth | Missing `verified.email` |
| Order/Wishlist | 3 | Auth | Missing `verified.email` |
| Order/Ordering | 8 | Auth | Missing `verified.email`, role middleware |
| Order/Voucher | 10 | Mixed | **Voucher show is public (no auth)**, duplicate legacy routes |
| Order/Reviews | 5 | Auth | OK |
| Order/Payment | 2 | No auth (webhook) | No rate limiting |
| Communication | 6 | Auth | OK |
| Support/Tickets | 6 | Auth | OK |
| Engagement/Missions | 5 | Auth | OK |
| Admin/Notifications | 4 | Auth (admin) | OK |

---

## 5. Frontend Audit

### 5.1 Pages Coverage

**Buyer/Public:**
- ✅ Home page
- ✅ Search page
- ✅ Category page
- ✅ Product detail page
- ✅ Cart page
- ✅ Checkout page
- ⚠️ Order detail page — **redirects to /cart with tab=order parameter** (not a standalone page)
- ✅ Profile page
- ✅ Addresses page
- ✅ Wishlist (redirects to /cart with tab=wishlist)
- ✅ Notifications page
- ✅ Payments page (displays order-based payment history)
- ✅ Vouchers page (shows all active vouchers, not user-specific claimed vouchers)
- ✅ Help page (full CRUD for support tickets)
- ✅ Missions page
- ✅ Chat page (basic thread list + window)
- ✅ Role switch page
- ✅ Seller onboarding page

**Seller:**
- ✅ Dashboard
- ✅ Products (full CRUD)
- ✅ Store settings
- ✅ Store preview
- ✅ Orders
- ✅ Vouchers
- ✅ Promotions
- ✅ Banners
- ✅ Showcase
- ✅ Finance (cashflow + receivables)
- ✅ Stock
- ✅ Customers
- ✅ Reviews
- ✅ Help
- ✅ Promotion payments
- ✅ Order operations
- ⚠️ Categories — **ModulePlaceholderPage** (placeholder, not implemented)
- ⚠️ Catalog groups — **ModulePlaceholderPage** (placeholder, not implemented)
- ⚠️ Users — **ModulePlaceholderPage** (placeholder, not implemented)
- ⚠️ Store management — **ModulePlaceholderPage** (placeholder, not implemented)

**Admin:**
- ✅ Dashboard
- ✅ Products
- ✅ Stores (CRUD + status)
- ✅ Users (CRUD + roles)
- ✅ Roles (CRUD + permissions)
- ✅ Categories (full CRUD)
- ✅ Catalog groups (full CRUD)
- ✅ Orders
- ✅ Vouchers
- ✅ Promotions
- ✅ Banners
- ✅ Showcase
- ✅ Finance
- ✅ Stock
- ✅ Customers
- ✅ Reviews
- ✅ Missions
- ✅ Help
- ✅ Announcements
- ✅ Promotion payments
- ✅ Order operations
- ⚠️ Store information — **ModulePlaceholderPage**
- ⚠️ Store preview — **ModulePlaceholderPage**

### 5.2 Frontend Issues

1. **OrderDetailPage.jsx** is a 6-line redirect to `/cart?tab=order&orderId=...` — no standalone order detail UI
2. **ProfileOrdersPage.jsx** exists but has no route — orders are accessed via `/cart?tab=order`
3. **Seller categories/catalog-groups/users/store-management** all show placeholder pages
4. **Voucher claim feature** is missing — VouchersPage shows all public vouchers, no "claim" functionality for user-specific vouchers
5. **No avatar upload** on profile page
6. **OrdersPage.jsx** is a 1-line re-export of ProfileOrdersPage (unnecessary indirection)
7. **Two geocoding services** exist (openStreetMapService.js and geocodingService.js) — duplicated code
8. **Firebase config** exposed in .env files committed to git
9. **No tests** anywhere in the frontend codebase
10. **No TypeScript strict mode** — all components are .jsx despite having tsconfig.json

---

## 6. Android Audit

### 6.1 Architecture
- **Package:** `com.example.luminasdgs`
- **Tech:** Kotlin + Jetpack Compose + Retrofit + Moshi
- **Structure:** data/remote, data/local, viewmodel, ui/screen, ui/theme

### 6.2 Current Functionality
The Android app is an **SDG Environmental Education Game**, NOT a marketplace app:

**5 Mini-Games:**
1. **Quiz** — Environmental questions, 60s timer, difficulty levels
2. **Trash Sort** — Drag-drop trash into correct bins (Organik/Anorganik/B3/Kertas/Residu)
3. **Myth or Fact** — Judge environmental statements as true/false
4. **SDG Match** — Match SDG statements to goals
5. **Clean River** — Take trash from river, avoid fish

**Additional Features:**
- Virtual tree growth (water/fertilize)
- Profile with hero card
- Achievement hub
- API settings (emulator/local/production)

### 6.3 API Integration (Minimal)

| Endpoint | Used? | Status |
|---|---|---|
| `identity/auth/password-login` | Yes (AuthRepository) | Working |
| `identity/auth/password-register` | Yes | Working |
| `identity/auth/me` | Yes (ProfileViewModel) | Working |
| `identity/auth/logout` | Yes | Working |
| `catalog/products` | Defined but NOT called | Dead code |
| `catalog/products/{id}` | Defined but NOT called | Dead code |
| `catalog/products/slug/{slug}` | Defined but NOT called | Dead code |
| `catalog/categories` | Defined but NOT called | Dead code |
| `order/carts` | Defined but NOT called | Dead code |
| `order/carts/items` | Defined but NOT called | Dead code |
| `order/carts/items/{id}` (PATCH) | Defined but NOT called | Dead code |
| `order/carts/items/{id}` (DELETE) | Defined but NOT called | Dead code |
| `order/orderings` (GET) | Defined but NOT called | Dead code |
| `order/orderings` (POST) | Defined but NOT called | Dead code |
| `engagement/missions` | Defined but NOT called | Dead code |
| `engagement/missions/me` | Defined but NOT called | Dead code |
| `identity/users/{id}` (GET) | Defined but NOT called | Dead code |
| `identity/users/{id}` (PUT) | Defined but NOT called | Dead code |

### 6.4 Critical Android Issues

1. **No marketplace functionality** — Product, cart, order APIs are defined but never called
2. **All game data is hardcoded** — Quests use `QuestDummyData`, not the backend mission API
3. **No daily mission auto-update on game completion** — Game completion does NOT report to backend
4. **No voucher system** — Zero voucher-related code
5. **No token refresh** mechanism
6. **Token stored in plain SharedPreferences** — Not EncryptedSharedPreferences
7. **HttpLoggingInterceptor.Level.BODY** enabled unconditionally — logs auth tokens in production
8. **No error boundaries** — API calls catch generic `Exception`
9. **GameState not persisted** — all game progress lost on process death
10. **Hardcoded production URL placeholder** — `https://your-production-domain.com/api/v1/`
11. **Two package structures** exist (`com.example.sdgs` and `com.example.luminasdgs`)
12. **No Room database** — no local persistence at all

---

## 7. Cross-Platform Inconsistency

### 7.1 Backend ↔ Frontend

| Issue | Backend | Frontend | Impact |
|---|---|---|---|
| Seller product costing routes | Only admin routes exist | Calls `/catalog/seller/products/{id}/costing` | **404 for sellers** |
| Order detail page | Full API available | Redirect to /cart tab | **Poor UX** |
| Voucher claim | Has `user_vouchers` table | No claim UI | **Feature gap** |
| User avatar upload | API exists | No upload component | **Feature gap** |
| Seller categories admin | Placeholder page | Shows ModulePlaceholderPage | **Broken** |
| Duplicate voucher routes | Legacy routes shadow new ones | Never calls legacy | **Dead code** |

### 7.2 Backend ↔ Android

| Issue | Backend | Android | Impact |
|---|---|---|---|
| Game completion event | No API endpoint | No code to call | **No integration** |
| Mission progress | `recordEvent()` exists | Uses dummy data | **Fake missions** |
| Product listing | Full API | API defined but not called | **Dead code** |
| Cart operations | Full API | API defined but not called | **Dead code** |
| Order creation | Full API | API defined but not called | **Dead code** |
| Voucher system | Full backend | No code at all | **Missing** |
| Chat | WebSocket + REST | No code | **Missing** |
| Profile update | Full API | Basic profile only | **Partial** |

### 7.3 Frontend ↔ Android

| Issue | Frontend | Android | Impact |
|---|---|---|---|
| Entire marketplace | Full UI | Zero UI | **No overlap** |
| Game/missions | Admin management only | Full game UI | **No overlap** |
| Auth flow | Full auth + Firebase | Basic auth only | **Different scope** |

---

## 8. API Contract Problems

### Critical Mismatches

**1. Seller Product Costing — 404**
```
Frontend: GET /api/v1/catalog/seller/products/{id}/costing
Backend:  No route defined in seller/products group
Status:   BROKEN — sellers cannot view/edit product costing
```

**2. Voucher Show Endpoint — Unauthenticated**
```
Backend:  GET /api/v1/order/vouchers/{id} — no auth middleware
Impact:   Anyone can enumerate voucher codes, discount values, limits
Severity: CRITICAL security issue
```

**3. Duplicate Voucher Routes**
```
Backend:  Two identical route groups for admin vouchers (prefixed and unprefixed)
Impact:   Shadowed routes, maintenance confusion
Severity: LOW
```

### Verified Correct Contracts (no issues)
- Auth (login, register, firebase, switch-role, logout, me, forgot-password)
- Cart (CRUD)
- Ordering (CRUD, status, cancel, shipping)
- Addresses (CRUD, resolve-destination)
- Wishlist (CRUD)
- Reviews (CRUD, product reviews)
- Products (public listing, detail, variants, attributes)
- Categories (tree, menu, path, products)
- Catalog groups (listing, detail, slug)
- Stores (public listing, detail, slug, management)
- Banners (admin, seller, public)
- Promotions (public, manage, approve, reject)
- Finance (CRUD, payments)
- Stock (movements, adjustments)
- Inventory (materials, material stock, cost impacts)
- Showcase (CRUD, public)
- Customers (listing)
- Tickets (CRUD, replies, status, context)
- Missions (CRUD, user missions)
- Communication (conversations, messages, read, announcements)
- Admin notifications (listing, state, read, read-all)
- Spreadsheet (template, export, import preview, import, bulk-delete)

---

## 9. Database Problems

### Missing Constraints
1. `mission_user_progress` — no unique constraint on `(mission_id, user_id)` → can create duplicate progress records
2. `chat_message_reads` — no unique constraint on `(message_id, user_id)` → can mark read multiple times
3. `product_costings` — no foreign key to `products` table
4. `raw_material_cost_histories` — no foreign key to `raw_materials`

### Unused/Questionable Fields
5. `products.sku` column exists but `product_variants.sku` is the primary SKU used everywhere
6. `products.weight` and `products.length/width/height` exist but `product_variants` also has these — potential confusion
7. `orders.voucher_id` is nullable but no logic handles partial voucher failures

### Migration Issues
8. `set_existing_users_testing_password` and `correct_existing_users_testing_password` — two sequential migrations that modify user passwords. These are dev/testing migrations that should not exist in production
9. No soft delete on `orders`, `order_items`, `payments` — data integrity risk if records are accidentally deleted
10. `users.avatar` is nullable string but no file validation or storage configuration visible

---

## 10. Business Logic Problems

### 10.1 Order Flow

**Issue: TOCTOU Race Condition in Order Creation**
```
Location: CreateOrderUseCase.php:75 (stock check) vs :210 (stock decrement)
Problem:  Stock is checked OUTSIDE the transaction, then decremented INSIDE.
          Between check and decrement, concurrent requests can deplete stock.
Impact:   Buyer goes through full checkout only to get generic error.
Fix:      Move stock check inside the transaction or use lockForUpdate.
```

**Issue: Duplicate Payment Records**
```
Location: ProcessPaymentUseCase.php:28-34
Problem:  Every call creates a new payments row. If user clicks "pay" twice
          or webhook fires twice, duplicate pending records are created.
Impact:   Confusing payment history, potential double-charging if logic is wrong.
Fix:      Add idempotency check (find existing pending payment for order).
```

### 10.2 Voucher Flow

**Issue: Voucher Usage Race Condition**
```
Location: CreateOrderUseCase.php:296-297, :372
Problem:  usage_limit check reads count before increment. Under concurrent
          requests, two orders can both pass the check and exceed the limit.
Fix:      Use lockForUpdate + re-check after lock, or use atomic check-and-increment.
```

**Issue: No Voucher Claim UI on Frontend**
```
Backend has user_vouchers table with status tracking.
Frontend VouchersPage shows ALL public vouchers, not user-claimed vouchers.
Missing: "Claim" button, voucher wallet management, user-specific voucher listing.
```

### 10.3 Stock & HPP

**Issue: Raw Material Cost Average Not Affected by Purchase Price on Import**
```
When importing raw materials via spreadsheet, average_cost is only set on CREATE.
On UPDATE, changes to average_cost are REJECTED.
This is correct behavior (weighted average should use stock movement), but the
import UI does not guide users to use raw-material-stock module instead.
```

**Issue: HPP Import Does NOT Match by SKU**
```
HPP import matches by product_id or product_name, NOT by SKU as requested.
User expectation: "import HPP di bentuk dengan menyamakan nama bahan baku dan
product berdasarkan sku"
Actual: HPP matches by product_id/product_name. Material matching is by raw_material.code (SKU).
Impact: User must know product_id or exact product name, not SKU.
```

### 10.4 Chat & Ban Notification

**Issue: No System Message on Ban/Unban**
```
When admin bans a user via UserController::update():
1. banned_at is set on the user
2. Middleware immediately blocks all API access
3. NO system message is sent to any conversation

Requirement: "pastikan chat masuk saat admin melakukan ban atau unbanned kepada user.
jika seller maka masuk ke chat toko bukan seller."

Status: NOT IMPLEMENTED
```

### 10.5 Daily Mission & Game Completion

**Issue: No Backend API for Game Completion Reporting**
```
Backend mission system uses internal recordEvent() calls.
Android game completion does NOT call any backend endpoint.
No "game_completed" or "game_scored" event type exists.

Requirement: "android seharusnya saat game selesai dimainkan maka otomatis
memperbarui progress daily mission nya ya. jadi bisa di claim."

Status: NOT IMPLEMENTED
```

**Issue: Android Uses Dummy Mission Data**
```
HomeViewModel uses QuestDummyData, not the backend API.
TreeViewModel has hardcoded TreeQuestMissionSection missions.
Backend engagement/missions/me endpoint exists but Android never calls it.
```

---

## 11. Security Problems

### CRITICAL

**S1. IDOR on User Profiles**
```
Location:   Identity/User/Presentation/routes.php:18-20
Endpoint:   GET /api/v1/identity/users/{id}
            PUT /api/v1/identity/users/{id}
Attack:     Any authenticated buyer can GET/PUT any user's profile by ID.
            Can read email, phone, avatar, roles of any user.
            Can UPDATE another user's name, email, phone, etc.
Severity:   CRITICAL — mass data exposure + data tampering
Fix:        Add ownership check: if (!admin) verify $id === auth()->id()
```

**S2. Unauthenticated Voucher Enumeration**
```
Location:   Order/Voucher/Presentation/routes.php:43
Endpoint:   GET /api/v1/order/vouchers/{id}
Attack:     Unauthenticated user can enumerate all voucher IDs.
            Reveals: code, discount value, type, min spend, usage limits,
            expiry dates, store restrictions.
Severity:   CRITICAL — full voucher data exposure
Fix:        Add auth:sanctum middleware
```

**S3. Voucher Public Listing Leaks All Codes**
```
Location:   Order/Voucher/Presentation/routes.php:9
Endpoint:   GET /api/v1/order/vouchers (no auth)
Attack:     Anyone can browse all active voucher codes and details.
Severity:   HIGH — voucher abuse, code scraping
Fix:        Either require auth or only show masked codes
```

### HIGH

**S4. No Rate Limiting on Midtrans Webhook**
```
Location:   Order/Payment/Presentation/routes.php:8-10
Endpoint:   POST /api/v1/order/payments/midtrans/notification
Attack:     DoS via repeated invalid payloads (each loads full Laravel stack).
            If server_key leaks, full order manipulation possible.
Severity:   HIGH
Fix:        Add rate limiting + IP whitelist
```

**S5. HttpLoggingInterceptor.Level.BODY in Android**
```
Location:   ApiClient.kt
Problem:    Full request/response body logging enabled unconditionally.
            Logs auth tokens, passwords, sensitive data in production.
Severity:   HIGH — token leakage via device logs
Fix:        Set to Level.NONE in release builds, or at minimum Level.BASIC
```

**S6. Token Stored in Plain SharedPreferences**
```
Location:   AuthRepository.kt
Problem:    Auth token stored in plain SharedPreferences ("api_config_prefs").
            Any app with root access can read it.
Severity:   HIGH
Fix:        Use EncryptedSharedPreferences
```

**S7. Mass Assignment Risk in Voucher Model**
```
Location:   Voucher.php:25-43
Problem:    $fillable includes used_count, store_id, created_by.
            If any code path uses Voucher::create($request->all()),
            attacker can reset used_count to 0 or change store_id.
Severity:   HIGH
Fix:        Remove used_count from $fillable, or ensure mass assignment is never used
```

### MEDIUM

**S8. Missing verified.email on Order/Cart/Wishlist/Address Routes**
```
Multiple critical routes allow unverified email users to perform actions.
Impact: Unverified users can place orders, manage carts, etc.
```

**S9. SwitchRoleUseCase — No Token Revocation**
```
Each role switch issues a new token. Old tokens are not revoked.
An attacker can accumulate valid tokens for different roles.
```

**S10. ProcessPaymentUseCase — No Order Ownership Verification**
```
The use case accepts any orderNumber without verifying it belongs to the authenticated user.
Controller-level check exists but no defense-in-depth.
```

**S11. Conversation IDOR Risk**
```
Conversation routes rely on ConversationPolicy but policy enforcement
depends on controller calling $this->authorize(). If not called,
any user with chat.use permission can access any conversation.
```

**S12. Firebase Token Logged on Failure**
```
ValidateFirebaseToken.php logs exception message which may contain raw token.
```

---

## 12. Bugs

### P0 — Critical

**BUG-001: IDOR — Any User Can Update Any User Profile**
```
Severity:   P0 (Critical)
Feature:    User Profile
Location:   app/Domains/Identity/User/Presentation/routes.php:18-20
Problem:    GET/PUT /users/{id} has no ownership check outside admin middleware group.
Expected:   Only admin can access other users' profiles. Users can only access their own.
Actual:     Any authenticated user can read/update any user by ID.
Root Cause: Routes registered outside admin middleware group without ownership check.
Impact:     Mass data tampering, privacy breach, account takeover.
Fix:        Add middleware or controller check: if (!auth()->user()->hasRole('admin') && $id !== auth()->id()) abort(403);
```

**BUG-002: Seller Product Costing Routes Missing**
```
Severity:   P0 (Critical)
Feature:    Product Costing (HPP)
Location:   app/Domains/Catalog/Product/Presentation/routes.php
Problem:    Frontend calls /catalog/seller/products/{id}/costing but backend
            only defines costing routes in the admin group.
Expected:   Seller can view/edit product costing.
Actual:     Seller gets 404 on all costing operations.
Root Cause: Missing route definitions in seller/products middleware group.
Impact:     Complete HPP management break for sellers.
Fix:        Add costing routes to seller/products route group.
```

**BUG-003: Voucher Endpoint Exposed Without Authentication**
```
Severity:   P0 (Critical)
Feature:    Voucher
Location:   app/Domains/Order/Voucher/Presentation/routes.php:43
Problem:    GET /vouchers/{id} has zero authentication middleware.
Expected:   Voucher details should require authentication.
Actual:     Anyone can enumerate all voucher details via ID.
Root Cause: Route registered outside auth middleware group.
Impact:     Full voucher data leakage (codes, limits, expiry).
Fix:        Move route inside auth middleware group.
```

**BUG-004: Order Detail Page Not Implemented**
```
Severity:   P0 (Critical)
Feature:    Order Detail
Location:   src/features/order/ordering/pages/OrderDetailPage.jsx
Problem:    OrderDetailPage is a 6-line redirect to /cart?tab=order&orderId=...
Expected:   Dedicated order detail page with status tracking, timeline, actions.
Actual:     Redirects to cart page tab — confusing UX, no standalone URL.
Root Cause: Page was never implemented, used redirect as placeholder.
Impact:     Users cannot share/bookmark order detail URLs. Poor UX.
Fix:        Implement proper OrderDetailPage with full order info.
```

### P1 — High

**BUG-005: Race Condition in Stock Decrement During Checkout**
```
Severity:   P1 (High)
Feature:    Checkout / Stock
Location:   CreateOrderUseCase.php:75 vs :210
Problem:    Stock checked outside transaction, decremented inside transaction.
Expected:   Stock check and decrement should be atomic.
Actual:     Concurrent requests can pass the check but fail the decrement.
Root Cause: TOCTOU (Time-of-Check-to-Time-of-Use) race condition.
Impact:     Users go through full checkout flow only to fail at order creation.
Fix:        Wrap stock check + decrement inside same DB transaction with lockForUpdate.
```

**BUG-006: Duplicate Payment Records on Multiple Submissions**
```
Severity:   P1 (High)
Feature:    Payment
Location:   ProcessPaymentUseCase.php:28-34
Problem:    No idempotency check — every call creates new payments row.
Expected:   Existing pending payment should be returned, not duplicated.
Actual:     Multiple pending payment records for same order.
Root Cause: Missing idempotency guard.
Impact:     Confusing payment history, potential double-charging.
Fix:        Check for existing pending payment before creating new one.
```

**BUG-007: No Chat Message When Admin Bans/Unbans User**
```
Severity:   P1 (High)
Feature:    Ban User / Chat
Location:   UserController::update()
Problem:    Ban action sets banned_at but sends no message to chat.
Expected:   System message injected into user's chat when banned/unbanned.
Actual:     User is silently blocked with no notification in chat.
Root Cause: Feature not implemented.
Impact:     User has no visibility into why they cannot access the platform.
Fix:        Add ConversationService->announceBanMessage() call in UserController::update().
```

**BUG-008: No Android Game Completion → Mission Progress Integration**
```
Severity:   P1 (High)
Feature:    Daily Mission / Game
Location:   Android GameViewModel, HomeViewModel
Problem:    Game completion does not report to backend mission system.
Expected:   After game completion, mission progress should auto-update.
Actual:     Games use local dummy data. Backend missions are disconnected.
Root Cause: No API endpoint for game events + Android doesn't call mission API.
Impact:     Daily missions never progress from gameplay.
Fix:        1. Add game_completed event type to backend
            2. Add POST /engagement/missions/report endpoint
            3. Android calls endpoint after game completion
```

**BUG-009: Frontend Shows All Public Vouchers Instead of User-Claimed Vouchers**
```
Severity:   P1 (High)
Feature:    Voucher
Location:   src/features/profile/vouchers/pages/VouchersPage.jsx
Problem:    VouchersPage calls GET /vouchers (public endpoint showing all active vouchers).
Expected:   Show vouchers that the user has claimed/saved to their account.
Actual:     Shows all marketplace vouchers regardless of claim status.
Root Cause: No user-specific voucher endpoint called; claim functionality missing.
Impact:     "Voucher Saya" page shows wrong data. Users see vouchers they haven't claimed.
Fix:        Add user-specific voucher listing endpoint and claim UI.
```

**BUG-010: Android HttpLoggingInterceptor Logs Full Bodies Including Tokens**
```
Severity:   P1 (High)
Feature:    Security
Location:   market-game/app/.../data/remote/ApiClient.kt
Problem:    HttpLoggingInterceptor.Level.BODY enabled unconditionally.
Expected:   No body logging in release builds.
Actual:     Full request/response bodies logged including auth tokens and passwords.
Root Cause: No build-type conditional logging configuration.
Impact:     Sensitive data in device logs accessible to other apps on rooted devices.
Fix:        Use BuildConfig.DEBUG to conditionally set log level.
```

**BUG-011: Android Token Stored in Plain SharedPreferences**
```
Severity:   P1 (High)
Feature:    Security
Location:   market-game/app/.../data/local/AuthRepository.kt
Problem:    Auth token stored in plain "api_config_prefs" SharedPreferences.
Expected:   Use EncryptedSharedPreferences for sensitive data.
Actual:     Token readable by any app with storage access on rooted devices.
Root Cause: No encryption configured for SharedPreferences.
Impact:     Token theft on compromised devices.
Fix:        Use EncryptedSharedPreferences from AndroidX Security library.
```

### P2 — Medium

**BUG-012: Seller Categories Page Shows Placeholder**
```
Severity:   P2 (Medium)
Feature:    Seller Categories
Location:   App.jsx — /seller/categories → ModulePlaceholderPage
Problem:    Seller cannot manage their product categories.
Expected:   Category management page for seller.
Actual:     Shows "Coming Soon" placeholder.
Impact:     Seller cannot self-categorize products.
```

**BUG-013: Seller Catalog Groups Page Shows Placeholder**
```
Severity:   P2 (Medium)
Feature:    Seller Catalog Groups
Location:   App.jsx — /seller/catalog-groups → ModulePlaceholderPage
```

**BUG-014: Admin Store Information Shows Placeholder**
```
Severity:   P2 (Medium)
Feature:    Admin Store Info
Location:   App.jsx — /admin/store-information → ModulePlaceholderPage
```

**BUG-015: Admin Store Preview Shows Placeholder**
```
Severity:   P2 (Medium)
Feature:    Admin Store Preview
Location:   App.jsx — /admin/store-preview → ModulePlaceholderPage
```

**BUG-016: Duplicate Geocoding Services**
```
Severity:   P2 (Medium)
Feature:    Address / Maps
Location:   openStreetMapService.js + geocodingService.js
Problem:    Two nearly identical geocoding service files exist.
Impact:     Maintenance confusion, potential inconsistent behavior.
```

**BUG-017: Voucher Duplicate Legacy Routes**
```
Severity:   P2 (Medium)
Feature:    Voucher
Location:   Order/Voucher/Presentation/routes.php:22-30
Problem:    Two identical route groups for admin vouchers.
Impact:     Dead code, maintenance confusion.
```

**BUG-018: ProfileOrdersPage Has No Route**
```
Severity:   P2 (Medium)
Feature:    Order History
Location:   src/features/profile/orders/pages/OrdersPage.jsx
Problem:    ProfileOrdersPage exists but /profile/orders redirects to /cart?tab=order.
Impact:     Code exists but is unreachable via routing.
```

**BUG-019: OrdersPage.jsx is Pointless Re-export**
```
Severity:   P2 (Medium)
Feature:    Order History
Location:   src/features/profile/orders/pages/OrdersPage.jsx
Problem:    1-line file that just re-exports ProfileOrdersPage.
Impact:     Unnecessary indirection.
```

**BUG-020: Missing verified.email on Critical Routes**
```
Severity:   P2 (Medium)
Feature:    Authentication
Location:   Multiple route files
Problem:    Cart, wishlist, ordering, address routes missing verified.email.
Impact:     Unverified users can perform transactions.
```

**BUG-021: SwitchRoleUseCase Doesn't Revoke Old Tokens**
```
Severity:   P2 (Medium)
Feature:    Authentication
Location:   SwitchRoleUseCase.php
Problem:    Issues new token on each switch without revoking old ones.
Impact:     Token accumulation, potential role confusion.
```

**BUG-022: Firebase Config Exposed in .env Files**
```
Severity:   P2 (Medium)
Feature:    Security
Location:   market-frontend/.env, .env.local
Problem:    Firebase API keys committed to git repository.
Impact:     Potential abuse of Firebase services.
```

### P3 — Low

**BUG-023: No Test Files in Entire Project**
```
Severity:   P3 (Low)
Feature:    Quality Assurance
Problem:    No unit tests, feature tests, or integration tests found.
Impact:     No regression protection, no verification of business logic.
```

**BUG-024: Android GameState Not Persisted**
```
Severity:   P3 (Low)
Feature:    Game
Problem:    All game progress lost on process death.
Impact:     Users lose progress if app is killed mid-game.
```

**BUG-025: Android Hardcoded Production URL Placeholder**
```
Severity:   P3 (Low)
Feature:    Configuration
Location:   BaseUrlManager.kt
Problem:    Production URL is "https://your-production-domain.com/api/v1/".
Impact:     Will fail in production until changed.
```

**BUG-026: Android Two Package Structures**
```
Severity:   P3 (Low)
Feature:    Architecture
Problem:    com.example.sdgs (template) and com.example.luminasdgs (actual) both exist.
Impact:     Confusion, potential classpath conflicts.
```

**BUG-027: Android Missing Room Database**
```
Severity:   P3 (Low)
Feature:    Architecture
Problem:    No local persistence — no offline support, no caching.
Impact:     App requires internet for all operations.
```

**BUG-028: Backend Testing Migrations in Codebase**
```
Severity:   P3 (Low)
Feature:    Database
Location:   set_existing_users_testing_password, correct_existing_users_testing_password
Problem:    Testing migrations exist in production migration set.
Impact:     Resets user passwords on migration run.
```

**BUG-029: No Avatar Upload on Frontend Profile**
```
Severity:   P3 (Low)
Feature:    User Profile
Problem:    Backend supports avatar upload but frontend has no upload component.
Impact:     Users cannot change their profile picture.
```

**BUG-030: ProductReviewsPage Seller Route Missing**
```
Severity:   P3 (Low)
Feature:    Reviews
Problem:    No dedicated seller reviews page (uses shared ReviewsPage).
Impact:     Minimal — shared page works but lacks seller-specific UX.
```

---

## 13. Missing Features

### Features in Roadmap but Not Implemented

| # | Feature | Roadmap Section | Status |
|---|---|---|---|
| 1 | Report System (Buyer/Seller/Admin) | 4. Moderation & Anti-Fraud | Missing — zero backend/frontend code |
| 2 | Anti-fraud System | 4. Moderation & Anti-Fraud | Missing |
| 3 | Audit Log (Admin activity) | 4. Moderation & Anti-Fraud | Missing |
| 4 | Warning/Suspend/Ban Content Moderation | 4. Moderation & Anti-Fraud | Partial — ban user exists, but no content moderation |
| 5 | Android Game → Mission Auto-Update | 8. Gamification | Missing — no integration |
| 6 | Android Voucher Integration | 7. Voucher | Missing — no code |
| 7 | Chat System Message on Ban/Unban | 6. Real-time Feature | Missing |
| 8 | Voucher Claim UI (Buyer) | 1.7 Voucher | Missing — public listing only |
| 9 | User Avatar Upload (Frontend) | 0. User Profile | Missing — backend ready |
| 10 | Export/Import for Etalase/Showcase | Spreadsheet | Missing — no module registered |
| 11 | Notification Order (Buyer push) | 5. Notification | Partial — admin notifications exist |
| 12 | Notification Stock Low/Out (Seller) | 5. Notification | Missing |
| 13 | Online/Offline Status | 6. Real-time Feature | Missing |
| 14 | Read Receipt | 6. Real-time Feature | Missing |
| 15 | Recommendation System | 10. Enhancement | Missing |
| 16 | Advanced Search | 10. Enhancement | Missing |
| 17 | Seller Performance Analytics | 10. Enhancement | Missing |

---

## 14. Partial Features

Features that exist but are incomplete:

| # | Feature | What Exists | What's Missing |
|---|---|---|---|
| 1 | Order Detail Page | API exists, redirect works | Standalone page with timeline, status tracking |
| 2 | Order History | Lists orders in cart tab | Dedicated page, filters, search |
| 3 | Voucher System | CRUD, checkout integration | Claim UI, user voucher wallet, expiry notifications |
| 4 | Daily Missions | Backend CRUD, admin UI, user listing | Game completion trigger, Android integration |
| 5 | User Profile | Basic CRUD, addresses | Avatar upload, notification preferences |
| 6 | Ban/Unban | Backend sets banned_at, frontend shows status | Chat notification, ban reason display to user |
| 7 | Android Game | 5 functional mini-games | Backend integration, mission progress, voucher rewards |
| 8 | Seller Dashboard | Metrics, recent orders | Stock alerts, low stock notifications |
| 9 | Admin Dashboard | User/store/order counts | Real-time stats, promotion pending, report pending |
| 10 | Chat | Thread list, message send/read | Image messages, offline messages, typing indicators |

---

## 15. Inconsistent Features

| # | Feature | Backend | Frontend | Android | Issue |
|---|---|---|---|---|---|
| 1 | Product Costing Routes | Admin only | Calls seller endpoint | N/A | Seller gets 404 |
| 2 | Voucher Visibility | Public listing + show endpoint | Shows all public vouchers | N/A | No user-specific filtering |
| 3 | Order Status Enum | pending/paid/processing/shipped/delivered/completed/cancelled | pending/processing/shipped/delivered/completed/cancelled (no "paid") | N/A | Frontend missing "paid" status |
| 4 | Chat Participants | Backend: direct/store/order/announcement types | Frontend: basic thread list | N/A | Backend richer than frontend displays |
| 5 | Mission Events | login/product_purchased/purchase_amount/order_completed/review_submitted | No game event | Dummy data | Android disconnected from backend |
| 6 | Product Listing | Full API with pagination, filtering, search | Full UI with grid/feed | API defined but unused | Android dead code |

---

## 16. Dead Code

### Backend
- Legacy voucher routes (routes.php lines 22-30) — shadowed by prefix group
- `set_existing_users_testing_password` migration — dev-only
- `correct_existing_users_testing_password` migration — dev-only
- Template domain — legacy cart system (still referenced by CartPricingService)

### Frontend
- `ProfileOrdersPage.jsx` — exists but unreachable via routing (redirected away)
- `OrdersPage.jsx` — 1-line re-export, unnecessary
- Duplicate geocoding service (`geocodingService.js` duplicates `openStreetMapService.js`)

### Android
- 10+ Retrofit API endpoints defined but never called (products, categories, cart, orders, missions, users)
- `GameViewModel.kt` — empty placeholder
- `com.example.sdgs` package — template leftover

---

## 17. Testing Coverage

### Backend
- **Unit Tests:** NONE (phpunit.xml exists but no test files found)
- **Feature Tests:** NONE
- **API Tests:** NONE
- **Authorization Tests:** NONE
- **Business Logic Tests:** NONE
- **Status:** UNTESTED — Complete absence of automated tests

### Frontend
- **Component Tests:** NONE
- **Integration Tests:** NONE
- **E2E Tests:** NONE
- **Status:** UNTESTED — No test framework configured

### Android
- **Unit Tests:** NONE (test directories exist but no test files)
- **ViewModel Tests:** NONE
- **Repository Tests:** NONE
- **API Tests:** NONE
- **Game Logic Tests:** NONE
- **Status:** UNTESTED — No test files found

**Overall Testing Status: 0% — COMPLETELY UNTESTED**

---

## 18. Export/Import (Spreadsheet) Audit

### Supported Modules (16)

| Module | Import | Export | Bulk Delete | Notes |
|---|---|---|---|---|
| product | ✅ | ✅ | ✅ | Supports with/without variants |
| category | ✅ | ✅ | ✅ | Up to Level 3, parent-child hierarchy |
| catalog-group | ✅ | ✅ | ✅ | Top-level grouping |
| raw-material | ✅ | ✅ | ❌ | average_cost only on create |
| raw-material-stock | ✅ | ✅ | ❌ | Stock movements |
| product-costing | ✅ | ✅ | ❌ | HPP by product_id/name, NOT SKU |
| stock | ✅ | ✅ | ❌ | Movement types, SKU matching |
| income | ✅ | ✅ | ✅ | Financial transactions |
| expense | ✅ | ✅ | ✅ | Financial transactions |
| receivable | ✅ | ✅ | ✅ | Piutang |
| payable | ✅ | ✅ | ✅ | Hutang |
| order | ✅ | ✅ | ✅ | Order data |
| promotion | ✅ | ✅ | ✅ | Campaign promotions |
| voucher | ✅ | ✅ | ✅ | Admin/seller vouchers |
| banner | ✅ | ✅ | ✅ | Store banners |
| customer | ❌ | ✅ | ❌ | Read-only |
| review | ❌ | ✅ | ❌ | Read-only |
| cost-impact | ❌ | ✅ | ❌ | Read-only |

### Product Import Details
- **Without variants:** Fill variant_name="Default", price, is_default=1
- **With variants:** Multiple rows with same store+product name, different variant data
- **Update mode:** Fill id column to update existing records
- **SKU:** Auto-generated if left empty

### HPP Import Details
- **Matching:** By product_id (preferred) or product_name (NOT by SKU as user expected)
- **Materials format:** `CODE:QTY|CODE:QTY` (matched by raw_material.code)
- **Calculated fields:** hpp and suggested_price are output-only (calculated by backend)

### Category Import Details
- **Level 1:** Leave parent_category_name empty, fill catalog_group_name
- **Level 2:** Fill parent_category_name with Level 1 name
- **Level 3:** Fill parent_category_name with Level 2 name (maximum)
- **Case-insensitive matching** prevents duplicates

### Etalase/Showcase Import — NOT SUPPORTED
No `etalase` or `showcase` module exists in the SpreadsheetModuleRegistry. This is a gap against the user's requirement.

---

## 19. Prioritized Fix Roadmap

### Sprint 1 — Critical (Security + Data Integrity)

| # | Fix | Severity | Est. Effort |
|---|---|---|---|
| 1 | Fix IDOR on user profiles (add ownership check) | P0/Security | 1h |
| 2 | Add auth to voucher show endpoint | P0/Security | 15min |
| 3 | Add seller product costing routes | P0 | 30min |
| 4 | Implement proper OrderDetailPage | P0 | 4h |
| 5 | Add rate limiting to Midtrans webhook | Security | 30min |
| 6 | Fix race condition in stock decrement (wrap in transaction) | P1 | 2h |
| 7 | Add idempotency check to ProcessPaymentUseCase | P1 | 1h |
| 8 | Fix Android body logging (conditional on DEBUG) | P1 | 30min |
| 9 | Fix Android token storage (EncryptedSharedPreferences) | P1 | 2h |

### Sprint 2 — Core Business (Missing Integrations)

| # | Fix | Severity | Est. Effort |
|---|---|---|---|
| 10 | Implement ban/unban chat notification | P1 | 3h |
| 11 | Add game completion API endpoint + event type | P1 | 4h |
| 12 | Android: integrate missions with backend API | P1 | 6h |
| 13 | Add voucher claim UI (buyer voucher wallet) | P1 | 4h |
| 14 | Add user-specific voucher listing endpoint | P1 | 1h |
| 15 | Add missing verified.email middleware to order routes | P2 | 30min |
| 16 | Revoke old tokens in SwitchRoleUseCase | P2 | 1h |
| 17 | Add idempotency to voucher usage check | P1 | 2h |

### Sprint 3 — Integration & UX

| # | Fix | Severity | Est. Effort |
|---|---|---|---|
| 18 | Implement seller categories page (remove placeholder) | P2 | 4h |
| 19 | Implement seller catalog groups page | P2 | 3h |
| 20 | Implement admin store information page | P2 | 3h |
| 21 | Implement admin store preview page | P2 | 2h |
| 22 | Add user avatar upload to frontend | P3 | 3h |
| 23 | Remove duplicate geocoding service | P2 | 30min |
| 24 | Remove duplicate legacy voucher routes | P2 | 15min |
| 25 | Clean up OrdersPage.jsx (remove pointless re-export) | P3 | 5min |
| 26 | Add user_vouchers unique constraint (mission_id, user_id) | P2 | 15min |
| 27 | Add chat_message_reads unique constraint | P2 | 15min |

### Sprint 4 — Business Features

| # | Fix | Severity | Est. Effort |
|---|---|---|---|
| 28 | Android: integrate product browsing | P3 | 8h |
| 29 | Android: integrate cart + ordering | P3 | 12h |
| 30 | Android: add voucher display/claim | P3 | 6h |
| 31 | Add Showcase/Etalase to spreadsheet modules | Feature | 4h |
| 32 | Add low stock notification for sellers | Feature | 4h |
| 33 | Implement notification system for buyer order status | Feature | 6h |
| 34 | Add stock check inside transaction lockForUpdate in recordCheckoutReservation | P1 | 2h |
| 35 | HPP import: add SKU-based matching option | Feature | 3h |

### Sprint 5 — Advanced

| # | Fix | Severity | Est. Effort |
|---|---|---|---|
| 36 | Implement Report System | Feature | 16h |
| 37 | Implement Anti-fraud System | Feature | 20h |
| 38 | Implement Audit Log | Feature | 8h |
| 39 | Add comprehensive test suite (backend) | Quality | 40h+ |
| 40 | Add frontend tests | Quality | 20h+ |
| 41 | Add Android tests | Quality | 16h+ |
| 42 | Online/Offline status | Feature | 8h |
| 43 | Read receipts | Feature | 4h |
| 44 | Recommendation system | Feature | 20h+ |
| 45 | Advanced search | Feature | 8h |

---

## 20. Final Verdict

### Overall Status

```
OVERALL STATUS: NOT READY

Roadmap Completion: ~52%

Backend:    ~78% Complete
            Strong DDD architecture, comprehensive business logic,
            but missing: test suite, report system, anti-fraud, audit log,
            ban chat notification, game completion event type, showcase spreadsheet module.

Frontend:   ~70% Complete
            Well-structured React app with full CRUD, but missing:
            proper order detail page, voucher claim UI, avatar upload,
            seller category/catalog-group management pages.

Android:    ~15% Complete
            Functional SDG game with 5 mini-games, but:
            zero marketplace integration, dummy mission data,
            no backend game→mission reporting, security issues.

Integration: ~45% Consistent
            Frontend↔Backend mostly aligned (2 critical mismatches found).
            Android↔Backend nearly disconnected (only auth works).

Critical Bugs:   4
High Bugs:       7
Medium Bugs:     11
Low Bugs:        8

Security Critical: 3
Security High:     4
Security Medium:   5
```

### Apakah Aplikasi Sudah Siap?

**NOT READY**

Alasan:
1. **3 security vulnerability kritis** yang memungkinkan data leakage dan data tampering
2. **4 bug P0** yang memutus fitur utama (product costing untuk seller, order detail, voucher security)
3. **Zero test coverage** di seluruh aplikasi — tidak ada jaminan regression-free
4. **Android hampir tidak terintegrasi** dengan marketplace — game berjalan sendiri dengan data dummy
5. **Missing core features**: ban chat notification, voucher claim, game→mission progress
6. **Race conditions** pada stock decrement dan voucher usage yang dapat menyebabkan kerugian finansial

### 10 Masalah Paling Penting yang Harus Diperbaiki Terlebih Dahulu

| # | Masalah | Dampak |
|---|---|---|
| 1 | **IDOR pada user profiles** — siapa saja bisa baca/update profile user lain | Privasi & keamanan akun seluruh user |
| 2 | **Voucher endpoint tanpa auth** — voucher codes bisa di-enumerate | Kecurangan voucher, kerugian finansial |
| 3 | **Seller product costing routes hilang** — seller dapat 404 | Fitur HPP tidak berjalan untuk seller |
| 4 | **Race condition stock decrement** — TOCTOU antara check dan decrement | Stok bisa minus atau order gagal setelah checkout |
| 5 | **Duplicate payment records** — tidak ada idempotency | Pembayaran ganda, data keuangan tidak akurat |
| 6 | **No ban chat notification** — user dibanned tanpa notifikasi | User tidak tahu akun dibanned, UX buruk |
| 7 | **Android game tidak terintegrasi mission** — data dummy | Daily mission tidak pernah progress dari gameplay |
| 8 | **Zero test coverage** — tidak ada automated tests | Setiap perubahan berisiko regression tanpa detection |
| 9 | **Order detail page tidak ada** — redirect ke cart tab | User tidak bisa lihat detail order secara proper |
| 10 | **Frontend voucher page menampilkan semua voucher publik** | "Voucher Saya" tidak menampilkan voucher yang sudah di-claim user |

---

## 21. Session: IAK/PPOB, Pricing, Finance, Games, Admin Store Context, Perf & Frontend

### Scope (per user directive)
- Fokus perbaikan & testing **frontend + backend**; Android di-skip untuk build/compile setelah perbaikan terakhir (user: *"skip android setelah perbaikan terakhir... tidak perlu melakukan run and build di android untuk sementara"*).
- Terapkan backend ke frontend marketplace (buyer PPOB + Admin Store Context).

### Backend — PPOB (Mobile Pulsa / IAK)
- Migration `2026_08_28_100000_create_ppob_tables.php` (PpoOperator, PpoProduct, PpoPricingRule, PpoOrder, PpoOrderItem, PpoTopUpStatus).
- `PricingEngine` — harga final = `cost + markup (%) + margin (fixed)` dari `PpoPricingRule`, per provider product code.
- `PpoOrderService` — buat order, hitung harga via PricingEngine, catat **finance ledger** secara idempotent, order success dengan fake top-up (mode development), penelusuran via `reference_id`.
- `IakProviderClient` — client IAK stage (`IAK_DEV_*`), path relatif (`/top-up`, `/check-status`, `/balance/check`, `/v1/bill/check`), kredensial dari env (tidak pernah hardcoded/logged).
- 25 route PPOB terdaftar di bawah `api/v1`.
- **Verifikasi**: integration test sukses — order sukses, 5 entri finance ledger, tidak ada secret bocor, lookup `reference_id` benar. `PpoOperator` provider_name default diperbaiki.

### Backend — Games
- `POST /engagement/games/report` (middleware `auth:sanctum,active.user,verified.email,throttle:60,1`, prefix `engagement/games`).
- `game_type` validated (whitelist `['arithmetic_kilat','sudoku']`), server **recompute** skor dari `questions[]` (arithmetic: 10 pts/correct; sudoku: 500 flat), reject duplikat session (unique `(user_id,game_type,session_id)`), reject duration mustahil, daily cap 50.
- HTTP 201 diterima / 409 duplikat / 422 reject. `GameSessionModel` tanpa SoftDeletes.
- **Verifikasi**: integration test — valid arithmetic diterima (50), duplikat ditolak, jawaban salah dihitung ulang (2/3→20), duration mustahil ditolak, sudoku valid 500, sudoku tidak valid ditolak.

### Backend — Admin Store Context + Perf/Cache
- Endpoint `/api/v1/admin/stores/context` (daftar toko) + `/{store}/stats|-order-trend|-orders|-products|-settlements`.
- Optimization: cache **scalar array** (TTL 300) — hanya tipe skalar (tidak pernah cache DTO/Model object) karena store `database` tidak bisa serialize object.
- **Verifikasi**: store 1 → 140 orders, 262.975.000 revenue, 100 products; dashboard 232 orders, fee 13000, top_stores 5; `cache:clear` dijalankan setelah perubahan entity shape.

### Frontend — PPOB (Buyer + Admin)
- `src/features/ppob/services/ppobService.js`: katalog buyer + admin (balance IAK, products, operators, pricing rules CRUD); `normalizePpobProduct` menyertakan `providerProductCode`.
- `src/features/ppob/pages/PpobPage.jsx`: halaman buyer (tabs Beli/Riwayat, kategori, pilih operator, grid produk, modal beli, filter status riwayat).
- `src/features/admin/ppob/pages/AdminPpobPage.jsx`: admin tabbed (Dashboard/Finance/Products/Operators/Pricing), stat cards, balance IAK, modal create produk/operator/rule, delete produk.
- Route `/ppob` + `/admin/ppob` ditambahkan di `App.jsx`; Navbar link "PPOB & Top Up" + `ADMIN_NAV_ITEMS`.

### Frontend — Admin Store Context
- `src/features/admin/storeContext/services/adminStoreContextService.js`: query stores/stats/order-trend/orders/products/settlements.
- `src/features/admin/storeContext/pages/AdminStoreContextPage.jsx`: store selector → Statistik/Tren/Pesanan/Produk/Settlement specifik toko.
- Route `/admin/store-context` + `ADMIN_NAV_ITEMS` "Monitoring Toko".

### Frontend — Build Verification
- `vite build` SUCCESS (2022 modules, ~2m9s), semua chunk baru ter-compile bersih. Hanya peringatan chunk-size (pre-existing, bukan error). eslint tidak tersedia di project → verifikasi via vite build.

### Android — Arithmetic Kilat & Sudoku (ditulis, TIDAK di-compile per user)
- `ArithmeticQuestion.kt`, `ArithmeticDummyData.kt`, `ArithmeticViewModel.kt`, `ArithmeticScreen.kt` (60s timer + 3s countdown, numpad, difficulty, CompletionDialog).
- `SudokuDummyData.kt` (valid grid + `isValidSolution`), `SudokuViewModel.kt`, `SudokuScreen.kt` (9x9, givens non-editable, numpad 1-9 + clear, 600s timer).
- `ApiService.kt` DTO (`GameReportRequest` dll) + `@POST("engagement/games/report")`; `GameDataRepository.reportArithmeticKilat/reportSudoku`; `Screen.kt`, `AppNavigation.kt`, `GameScreen.kt` terdaftar.
- Klien kirim **jawaban aktual user** per index → server recompute jujur. Rewards ditampilkan klien tapi server adalah sumber kebenaran.

### Sign-off
- PPOB & game backend integration tests lulus; PHP lint bersih; frontend production build lulus. Android build **tidak** dijalankan (skip per user). `cache:clear` dijalankan sesuai kebutuhan.

---

## 22. Session: Fixes Bug/Keamanan, Storage/Gambar, Seeder Seller Akbar, Test PHPUnit

### Migrasi Integration Test ke PHPUnit
- Dirikan 	ests/ + 	ests/TestCase.php + 	ests/CreatesApplication.php + 	ests/IntegrationTestCase.php.
- Config phpunit.integration.xml (MySQL, tanpa RefreshDatabase untuk hindari operasi destruktif; tes self-cleaning).
- 	ests/Feature/PPOB/PlacePpoOrderIntegrationTest.php + 	ests/Feature/Gaming/GameReportIntegrationTest.php.
- Hasil: OK (7 tests, 26 assertions).

### Race Condition & Idempotency Pembayaran
- ProcessPaymentUseCase::execute dibungkus DB::transaction + lockForUpdate pada baris order (OrderRepositoryInterface::findByOrderNumber(, )), sehingga exists-check + insert serial — cegah duplicate payment rows pada concurrent submit.

### Keamanan (Verifikasi / Sudah Aman)
- IDOR user profile: sudah diamankan UserController::ensureAccess (non-admin hanya akses dirinya). Tidak ada perubahan diperlukan.
- Voucher: semua write/claim/use sudah auth; hanya GET /order/vouchers (info publik) tanpa token. OK.
- Seller product costing routes: TERDAFTAR (admin+seller GET/PUT). 404 sebelumnya = server stale/deploy, bukan route hilang.

### Storage / Gambar (Root cause gambar kosong)
- Symlink/junction public/storage menunjuk target salah (D:\New folder\market-api\... bukan marketplace\market-api\...). Diperbaiki ke D:\New folder\marketplace\market-api\storage\app\public. Semua /storage/... (avatar, logo seller, produk, voucher) kini ter-resolve.
- Frontend profil avatar: render avatar asli (bukan hanya inisial) + tombol "Ubah Foto" kini berfungsi (upload ke /catalog/media/images, simpan vatar via PUT user, refresh). ProfileIdentityCard memakai esolveMediaUrl.

### Seeder Seller Akbar (realtime database)
- database/seeders/AkbarFahlevySellerSeeder.php (idempotent, additive), terdaftar di DatabaseSeeder.
- User kbarfahlevy39@gmail.com password 123: role seller+buyer, active+verified, store "Akbar Fahlevy Store" (approved/active).
- Data seller panel: 16 produk, 15 sub-order, 6 settlement, 16 financial_transactions, 30 stock_movements, 6 schedule, 4 withdrawal, payments, reviews, promotions, showcases.
- Verified: LoginUserUseCase dengan email/password/role=seller -> active_role seller + store {id:106, name:Akbar Fahlevy Store}; api_token + access_token terbit.

### Build/Health
- Backend: PHP lint bersih (semua file baru/diubah), cache:clear + config:clear OK, integration tests green.
- Frontend: `vite build` SUCCESS, ProfilePage ter-compile (hanya warning chunk-size pre-existing).
- Android: tetap SKIP build sesuai arahan user (kode game sudah ditulis sebelumnya).

---

## 23. Session: Frontend P1/P2 Production-Readiness (Perf, Dead Code, Cache Correctness)

### Ringkasan
Fokus sesi ini: menyelesaikan item P1/P2 frontend yang tersisa agar launch-ready — polling berlebihan, cache-key collisions, dead code halaman order/wishlist, invalidasi no-op pada kategori, dan pembersihan cache yang terlalu luas. Backend: verifikasi test suite green.

### Frontend — Polling & Query Health
- `src/core/api/publicQueryOptions.js`: `refetchInterval` diubah dari `30000` → `false` (menghentikan polling boros di ~33 query publik). Mempertahankan `staleTime: 0`, `refetchOnMount: "always"`, `refetchOnWindowFocus: true`. `PUBLIC_QUERY_REFRESH_MS` tetap diekspor.
- `src/features/order/voucher/services/voucherService.js`: `useActiveVouchers` memakai `refetchInterval: PUBLIC_QUERY_REFRESH_MS` (live) — satu-satunya query yang tetap polling.
- `src/features/admin/adminService.js`: hapus ekspor mati `useAdminCatalogGroups`/`useAdminCategories` + `flattenCategories`/`getCatalogGroupAdminRows`/`getCategoryAdminRows` dan unused imports — menghilangkan cache-key collision dengan `["admin","catalog-groups"]`/`["admin","categories"]` di `adminCatalogGroupService`/`adminCategoryService`.

### Frontend — Cache Correctness
- `src/features/catalog/category/services/categoryService.js`: `invalidateCategoryNavigationCache` (sebelumnya **no-op kosong**) kini benar-benar meng-invalidasi query navigation + menu (`["catalog","categories","navigation"]` / `["catalog","categories","menu"]`), menerima `queryClient` opsional.
- `src/features/catalog/application/cache/invalidateCatalogResources.js`: meneruskan `queryClient` ke fungsi tsb (menutup gap di mana path ini tidak meng-invalidasi query TanStack navigation).
- `src/features/admin/category/services/adminCategoryService.js`: `refreshCategoryQueries` meneruskan `queryClient` ke `invalidateCategoryNavigationCache`.
- `src/features/profile/identity/pages/ProfilePage.jsx`: ganti `queryClient.invalidateQueries()` tanpa filter (membersihkan seluruh cache) dengan scoped `["auth"]` setelah upload avatar; konsolidasi `useAuth()` ×3 menjadi satu panggilan; hapus `useMemo` tanpa manfaat.

### Frontend — Dead Code & UX
- Hapus 4 file halaman mati (tidak ada route/import yang mereferensikannya; diverifikasi via grep + build lulus):
  - `src/features/order/ordering/pages/ProfileOrdersPage.jsx`
  - `src/features/profile/orders/pages/OrdersPage.jsx` (re-export 1 baris)
  - `src/features/order/wishlist/pages/WishlistPage.jsx`
  - `src/features/profile/wishlist/pages/WishlistPage.jsx`
- `src/features/catalog/product/components/ProductCard.jsx`: `loading="lazy"` pada gambar kartu produk (hemat bandwidth di bawah fold).
- Error helpers divertifikasi sudah mendelegasikan ke satu `getApiMessage` (single source of truth) — tidak perlu konsolidasi lebih lanjut.

### Backend — Verifikasi
- `MidtransWebhookController` **divalidasi sebagai route LIVE** (terdaftar di `app/Domains/Order/Payment/Presentation/routes.php:9` untuk `payment.notification`) → **tidak dihapus**.
- Lint `php -l` bersih pada semua file domain yang diubah sesi sebelumnya (bulk reader cart, dashboard/store-context SQL aggregate, hutang-piutang, voucher repo, dll).
- **Test suite green: 29 passed, 82 assertions** (SQLite `:memory:`) lintas `tests/Feature/Auth`, `Catalog/CategoryCrudTest`, `Catalog/ProductCrudTest`, `Seller/StoreCrudTest`, `Order/VoucherCrudTest`, `Order/OrderFlowTest`, `Spreadsheet/SpreadsheetTransferTest` (CRUD + import/export + auth).
- Catatan: `tests/Feature/PPOB` & `tests/Feature/Gaming` tetap **dikecualikan** (butuh MySQL riil, bukan SQLite).

### Frontend — Build Verification
- `vite build` SUCCESS (2027 modules, tanpa error) setelah semua perubahan di atas.

### Sisa / Catatan
- Duplicate endpoint definitions menghasilkan cache query terpisah (mis. `/order/orderings`, `/seller/stores/manage`) — berfungsi tapi key tidak disatukan (risiko refactor > manfaat; tercatat utk ditindak lanjut).
- Koneksi MySQL `marketplaceku` sempat refusal saat startup — MySQL sudah listening; bila terulang di artisan command, jalankan ulang (kemungkinan race saat inisialisasi).
