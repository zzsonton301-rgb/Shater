package com.shater.invoices.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "items", indices = [Index(value = ["barcode"], unique = true)])
data class Item(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val barcode: String? = null, val quantity: Double = 0.0, val minQuantity: Double = 0.0, val purchasePrice: Double = 0.0, val salePrice: Double = 0.0)
@Entity(tableName = "contacts")
data class Contact(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val phone: String = "", val type: String, val balance: Double = 0.0)
@Entity(tableName = "invoices")
data class Invoice(@PrimaryKey(autoGenerate = true) val id: Long = 0, val number: String, val type: String, val contactId: Long? = null, val total: Double = 0.0, val paid: Double = 0.0, val createdAt: Long = System.currentTimeMillis())
@Entity(tableName = "invoice_lines", foreignKeys = [ForeignKey(entity = Invoice::class, parentColumns = ["id"], childColumns = ["invoiceId"], onDelete = ForeignKey.CASCADE)])
data class InvoiceLine(@PrimaryKey(autoGenerate = true) val id: Long = 0, val invoiceId: Long, val itemId: Long, val quantity: Double, val price: Double)
@Entity(tableName = "debts")
data class Debt(@PrimaryKey(autoGenerate = true) val id: Long = 0, val contactId: Long, val amount: Double, val dueDate: Long, val direction: String, val settled: Boolean = false)

data class InvoiceSummary(val id: Long, val number: String, val type: String, val total: Double, val createdAt: Long)
@Dao interface ItemDao { @Query("SELECT * FROM items ORDER BY name") fun observeAll(): Flow<List<Item>>; @Query("SELECT * FROM items WHERE quantity <= minQuantity") fun observeLowStock(): Flow<List<Item>>; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: Item); @Delete suspend fun delete(item: Item); @Query("UPDATE items SET quantity = quantity + :delta WHERE id = :id") suspend fun adjustQuantity(id: Long, delta: Double) }
@Dao interface ContactDao { @Query("SELECT * FROM contacts WHERE type = :type ORDER BY name") fun observe(type: String): Flow<List<Contact>>; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(contact: Contact) }
@Dao interface InvoiceDao { @Query("SELECT id,number,type,total,createdAt FROM invoices ORDER BY createdAt DESC") fun observeSummaries(): Flow<List<InvoiceSummary>>; @Insert suspend fun insert(invoice: Invoice): Long; @Insert suspend fun insertLine(line: InvoiceLine) }
@Dao interface DebtDao { @Query("SELECT * FROM debts WHERE settled = 0 ORDER BY dueDate") fun observeOpen(): Flow<List<Debt>>; @Insert suspend fun insert(debt: Debt) }
@Database(entities = [Item::class, Contact::class, Invoice::class, InvoiceLine::class, Debt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() { abstract fun itemDao(): ItemDao; abstract fun contactDao(): ContactDao; abstract fun invoiceDao(): InvoiceDao; abstract fun debtDao(): DebtDao
    companion object { @Volatile private var INSTANCE: AppDatabase? = null; fun get(context: android.content.Context) = INSTANCE ?: synchronized(this) { INSTANCE ?: Room.databaseBuilder(context, AppDatabase::class.java, "shater_invoices.db").build().also { INSTANCE = it } } }
}
