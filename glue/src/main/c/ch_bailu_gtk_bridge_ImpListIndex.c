/*
 * https://docs.gtk.org/gobject/tutorial.html
 */

#include "ch_bailu_gtk_bridge_ImpListIndex.h"
#include <glib-object.h>
#include <gio/gio.h>

static GType list_index_get_type (void);

typedef struct _ListIndex ListIndex;
typedef struct {
    GObjectClass parent_class;
} ListIndexClass;



typedef ListIndex *ListIndex_autoptr;
typedef GList *ListIndex_listautoptr;
typedef GSList *ListIndex_slistautoptr;
typedef GQueue *ListIndex_queueautoptr;

static __attribute__ ((__unused__)) inline void glib_autoptr_clear_ListIndex (ListIndex *_ptr)
{
    if (_ptr) (glib_autoptr_clear_GObject) ((GObject *) _ptr);
}

static __attribute__ ((__unused__)) inline void glib_autoptr_cleanup_ListIndex (ListIndex **_ptr) {
    glib_autoptr_clear_ListIndex (*_ptr);
}

static __attribute__ ((__unused__)) inline void glib_listautoptr_cleanup_ListIndex (GList **_l) {
    g_list_free_full (*_l, (GDestroyNotify) (void(*)(void)) glib_autoptr_clear_GObject);
}

static __attribute__ ((__unused__)) inline void glib_slistautoptr_cleanup_ListIndex (GSList **_l) {
    g_slist_free_full (*_l, (GDestroyNotify) (void(*)(void)) glib_autoptr_clear_GObject);
}

static __attribute__ ((__unused__)) inline void glib_queueautoptr_cleanup_ListIndex (GQueue **_q) {
    if (*_q) g_queue_free_full (*_q, (GDestroyNotify) (void(*)(void)) glib_autoptr_clear_GObject);
}


typedef ListIndexClass *ListIndexClass_autoptr;
typedef GList *ListIndexClass_listautoptr;
typedef GSList *ListIndexClass_slistautoptr;
typedef GQueue *ListIndexClass_queueautoptr;

static __attribute__ ((__unused__)) inline void glib_autoptr_clear_ListIndexClass (ListIndexClass *_ptr) {
    if (_ptr) (g_type_class_unref) ((ListIndexClass *) _ptr);
}

static __attribute__ ((__unused__)) inline void glib_autoptr_cleanup_ListIndexClass (ListIndexClass **_ptr) {
    glib_autoptr_clear_ListIndexClass (*_ptr);
}

static __attribute__ ((__unused__)) inline void glib_listautoptr_cleanup_ListIndexClass (GList **_l) {
    g_list_free_full (*_l, (GDestroyNotify) (void(*)(void)) g_type_class_unref);
}

static __attribute__ ((__unused__)) inline void glib_slistautoptr_cleanup_ListIndexClass (GSList **_l) {
    g_slist_free_full (*_l, (GDestroyNotify) (void(*)(void)) g_type_class_unref);
}

static __attribute__ ((__unused__)) inline void glib_queueautoptr_cleanup_ListIndexClass (GQueue **_q) {
    if (*_q) g_queue_free_full (*_q, (GDestroyNotify) (void(*)(void)) g_type_class_unref);
}

__attribute__ ((__unused__)) static inline ListIndex * LIST_INDEX (gpointer ptr) {
    return (((ListIndex*) g_type_check_instance_cast ((GTypeInstance*) (ptr), (list_index_get_type ()))));
}

__attribute__ ((__unused__)) static inline gboolean LIST_IS_INDEX (gpointer ptr) {
    return ((__extension__ ({ GTypeInstance *__inst = (GTypeInstance*) (ptr);
    GType __t = (list_index_get_type ());
    gboolean __r; if (!__inst) __r = (0);
    else if (__inst->g_class && __inst->g_class->g_type == __t) __r = (!(0)); else __r = g_type_check_instance_is_a (__inst, __t); __r; })));
}


ListIndex *list_index_new(void);



struct _ListIndex
{
    GObject parent_instance;

    int index;
    int size;
};


enum
{
    PROP_0,
    PROP_ITEM_TYPE,
    N_PROPERTIES
};


static void list_index_iface_init (GListModelInterface *iface);

static void list_index_init (ListIndex *self);
static void list_index_class_init (ListIndexClass *klass);
static GType list_index_get_type_once (void);
static gpointer list_index_parent_class = ((void *)0);
static gint ListIndex_private_offset;
static void list_index_class_intern_init (gpointer klass) {
    list_index_parent_class = g_type_class_peek_parent (klass);
    if (ListIndex_private_offset != 0) g_type_class_adjust_private_offset (klass, &ListIndex_private_offset);
    list_index_class_init ((ListIndexClass*) klass);
}

__attribute__ ((__unused__)) static inline gpointer list_index_get_instance_private (ListIndex *self) {
    return (((gpointer) ((guint8*) (self) + (glong) (ListIndex_private_offset))));
}


GType list_index_get_type (void) {
    static gsize static_g_define_type_id = 0;

    if ((__extension__ (
            {
                    _Static_assert (sizeof *(&static_g_define_type_id) == sizeof (gpointer), "Expression evaluates to false");
                    (void) (0 ? (gpointer) *(&static_g_define_type_id) : ((void *)0));

                    (!(__extension__ (
                    {
                        _Static_assert (sizeof *(&static_g_define_type_id) == sizeof (gpointer), "Expression evaluates to false");
                        __typeof__ (*(&static_g_define_type_id)) gapg_temp_newval;
                        __typeof__ ((&static_g_define_type_id)) gapg_temp_atomic = (&static_g_define_type_id);
                        __atomic_load (gapg_temp_atomic, &gapg_temp_newval, 5);
                        gapg_temp_newval;
                    })) && g_once_init_enter (&static_g_define_type_id));

            }))) {
        GType g_define_type_id = list_index_get_type_once ();
        (__extension__ (
                {
                        _Static_assert (sizeof *(&static_g_define_type_id) == sizeof (gpointer), "Expression evaluates to false");
                        0 ? (void) (*(&static_g_define_type_id) = (g_define_type_id)) : (void) 0;
                        g_once_init_leave ((&static_g_define_type_id), (gsize) (g_define_type_id));
                }));
    }
    return static_g_define_type_id;
}



__attribute__ ((__noinline__)) static GType list_index_get_type_once (void) {
    GType g_define_type_id = g_type_register_static_simple (((GType) ((20) << (2))), g_intern_static_string ("ListIndex"), sizeof (ListIndexClass), (GClassInitFunc)(void (*)(void)) list_index_class_intern_init, sizeof (ListIndex), (GInstanceInitFunc)(void (*)(void)) list_index_init, (GTypeFlags) 0);
    {
        {
            {
                const GInterfaceInfo g_implement_interface_info = {
                        (GInterfaceInitFunc)(void (*)(void)) list_index_iface_init, ((void *)0) , ((void *)0)
                };
                g_type_add_interface_static (g_define_type_id, g_list_model_get_type (), &g_implement_interface_info);
            };
        }
    }
    return g_define_type_id;
};



static void
list_index_items_changed (ListIndex *listIndex,
                          guint index,
                          guint removed,
                          guint added)
{
    g_list_model_items_changed (G_LIST_MODEL (listIndex), index, removed, added);
}


static void
list_index_dispose (GObject *object)
{
    ((((GObjectClass*) g_type_check_class_cast ((GTypeClass*) ((list_index_parent_class)), (((GType) ((20) << (2))))))))->dispose (object);
}


static void
list_index_get_property (GObject *object,
                         guint property_id,
                         GValue *value,
                         GParamSpec *pspec)
{
    if (property_id == PROP_ITEM_TYPE) {
        g_value_set_gtype(value, list_index_get_type());
    } else {
        do {
            GObject *_glib__object = (GObject*) ((object));
            GParamSpec *_glib__pspec = (GParamSpec*) ((pspec)); guint _glib__property_id = ((property_id));
            g_log (((gchar*) 0), G_LOG_LEVEL_WARNING, "%s:%d: invalid %s id %u for \"%s\" of type '%s' in '%s'", "src/main/c/ch_bailu_gtk_bridge_ImpListIndex.c", 64, ("property"), _glib__property_id, _glib__pspec->name, g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__pspec))->g_class))->g_type)))), (g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__object))->g_class))->g_type))))));
        } while (0);
    }
}


static void
list_index_set_property (GObject *object,
                         guint property_id,
                         const GValue *value,
                         GParamSpec *pspec)
{
    if (property_id != PROP_ITEM_TYPE) {
        do {
            GObject *_glib__object = (GObject*) ((object)); GParamSpec *_glib__pspec = (GParamSpec*) ((pspec));
            guint _glib__property_id = ((property_id));
            g_log (((gchar*) 0), G_LOG_LEVEL_WARNING, "%s:%d: invalid %s id %u for \"%s\" of type '%s' in '%s'", "src/main/c/ch_bailu_gtk_bridge_ImpListIndex.c", 76, ("property"), _glib__property_id, _glib__pspec->name, g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__pspec))->g_class))->g_type)))), (g_type_name ((((((GTypeClass*) (((GTypeInstance*) (_glib__object))->g_class))->g_type))))));
        } while (0);
    }
}


static void
list_index_class_init (ListIndexClass *klass)
{
    GObjectClass *object_class = ((((GObjectClass*) g_type_check_class_cast ((GTypeClass*) ((klass)), (((GType) ((20) << (2))))))));

    object_class->dispose = list_index_dispose;
    object_class->get_property = list_index_get_property;
    object_class->set_property = list_index_set_property;

    g_object_class_install_property (object_class, PROP_ITEM_TYPE,
                                     g_param_spec_gtype ("item-type", "", "", ((GType) ((20) << (2))),
                                                         G_PARAM_CONSTRUCT | G_PARAM_READWRITE | (G_PARAM_STATIC_NAME | G_PARAM_STATIC_NICK | G_PARAM_STATIC_BLURB)));
}


static GType
list_index_get_item_type (GListModel *list)
{
    return list_index_get_type();
}


static guint
list_index_get_n_items (GListModel *list)
{
    ListIndex *listIndex = LIST_INDEX (list);
    return (guint) listIndex->size;
}


static gpointer
list_index_get_item (GListModel *list,
                     guint index)
{
    ListIndex *result = list_index_new();
    ListIndex *listIndex = LIST_INDEX (list);

    listIndex->index = index;
    result->index = index;
    result->size = listIndex->size;

    return result;
}


static void
list_index_iface_init (GListModelInterface *iface)
{
    iface->get_item_type = list_index_get_item_type;
    iface->get_n_items = list_index_get_n_items;
    iface->get_item = list_index_get_item;
}


static void
list_index_init (ListIndex *listIndex)
{
    listIndex->index = 0;
    listIndex->size = 0;
}

ListIndex *
list_index_new(void)
{
    return g_object_new(list_index_get_type(), "item-type", list_index_get_type(), ((void *)0));
}

__attribute__((visibility("default"))) jlong
Java_ch_bailu_gtk_bridge_ImpListIndex_create(JNIEnv *env, jclass klass)
{
    return (jlong) list_index_new();
}

__attribute__((visibility("default"))) void
Java_ch_bailu_gtk_bridge_ImpListIndex_setSize (JNIEnv *env, jclass klass, jlong object, jint size)
{
    ListIndex *listIndex = (ListIndex*) object;

    guint old_size = (guint) listIndex->size;

    listIndex->size = (jint) size;
    list_index_items_changed (listIndex, 0, old_size, size);

}

__attribute__((visibility("default"))) jint
Java_ch_bailu_gtk_bridge_ImpListIndex_getIndex (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->index;
}


__attribute__((visibility("default"))) jint
Java_ch_bailu_gtk_bridge_ImpListIndex_getSize (JNIEnv *env, jclass klass, jlong object)
{
    ListIndex *listIndex = (ListIndex*) object;
    return (jint) listIndex->size;
}


__attribute__((visibility("default"))) void
Java_ch_bailu_gtk_bridge_ImpListIndex_setIndex (JNIEnv *env, jclass klass, jlong object, jint index)
{
    ListIndex *listIndex = (ListIndex*) object;
    listIndex->index = (int) index;
}
