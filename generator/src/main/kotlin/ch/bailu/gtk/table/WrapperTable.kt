package ch.bailu.gtk.table

import java.util.*

/**
 * cType to classType table.
 * Complex c-types are implemented manually as data structures in c-space.
 * These data structures are represented in Java-space by type-wrapper classes.
 */
object WrapperTable {
    private const val NAMESPACE = "type"

    private val table: MutableMap<String, String> = HashMap(50)

    init {
        add("unsigned char*", "Bytes")
        add("const double*", "Dbls")
        add("gchar*", "Str")
        add("const char*", "Str")
        add("const gchar*", "Str")
        add("filename", "Str")
        add("char**", "Strs")
        add("gint*", "Int")
        add("int*", "Int")
        add("gdouble*", "Dbls")
        add("gconstpointer", "Pointer")
        add("gpointer", "Pointer")
    }


    private fun add(ctype: String, wrapper: String) {
        table[ctype] = wrapper
        StructureTable.add(NAMESPACE, wrapper)
    }

    operator fun contains(cType: String): Boolean {
        return table.containsKey(cType)
    }

    fun convert(cType: String): String {
        return if (contains(cType)) {
            NAMESPACE + "." + table[cType]
        } else cType
    }
}