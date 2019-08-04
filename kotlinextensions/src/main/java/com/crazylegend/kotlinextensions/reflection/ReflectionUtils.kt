package com.crazylegend.kotlinextensions.reflection

import com.crazylegend.kotlinextensions.isNotNull
import com.crazylegend.kotlinextensions.isNull
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.tryOrNull
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.kotlinProperty


/**
 * Created by hristijan on 6/7/19 to long live and prosper !
 */


/**
 * Direct call a function of an object
 *
 * @param object
 * @param methodName
 * @param parameterTypes
 * @param parameters
 * @return
 * @throws NoSuchMethodException
 * @throws SecurityException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 * @throws InvocationTargetException
 */
@Throws(NoSuchMethodException::class, SecurityException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class)
fun Any.callMethod(methodName: String, parameterTypes: Array<Class<*>>, parameters: Array<Any>): Any? {
    val method = javaClass.getDeclaredMethod(methodName, *parameterTypes)
    method.isAccessible = true
    return method.invoke(this, parameters)
}


/**
 * Get DeclaredMethod
 *
 * @param object
 * @param methodName
 * @param parameterTypes
 */
fun Any.getDeclaredMethod(methodName: String, vararg parameterTypes: Class<*>): Method? {
    var method: Method?

    var clazz: Class<*> = javaClass
    while (clazz != Any::class.java) {
        try {
            method = clazz.getDeclaredMethod(methodName, *parameterTypes)
            return method
        } catch (e: Exception) {

        }

        clazz = clazz.superclass as Class<*>
    }

    return null
}


fun Any.invokeMethod(methodName: String, parameterTypes: Array<Class<*>>,
                     parameters: Array<Any>): Any? {
    val method = getDeclaredMethod(methodName, *parameterTypes)
    method!!.isAccessible = true
    try {
        return method.invoke(this, *parameters)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    }

    return null
}


fun Any.getDeclaredField(fieldName: String): Field? {
    var field: Field?

    var clazz: Class<*> = javaClass

    while (clazz != Any::class.java) {
        try {
            field = clazz.getDeclaredField(fieldName)
            return field
        } catch (e: Exception) {

        }

        clazz = clazz.superclass as Class<*>
    }

    return null
}


fun Any.setFieldValue(fieldName: String, value: Any) {

    val field = getDeclaredField(fieldName)

    field?.isAccessible = true

    try {
        field?.set(this, value)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

}


fun Any.getFieldValue(fieldName: String): Any? {

    val field = getDeclaredField(fieldName)

    field?.isAccessible = true

    try {
        return field?.get(this)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}


fun getObjectInstance(className: String, parameterTypes: Array<Class<*>>, parameters: Array<Any>): Any? {
    try {
        val cls = Class.forName(className)
        val cons = cls.getDeclaredConstructor(*parameterTypes)
        cons.isAccessible = true
        return cons.newInstance(parameters)
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    }

    return null
}

fun getConstructor(className: String, parameterTypes: Array<Class<*>>): Constructor<*>? {

    try {
        val cls = Class.forName(className)
        val cons = cls.getDeclaredConstructor(*parameterTypes)
        cons.isAccessible = true
        return cons
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    }

    return null
}

/**
 * Finds first property if the name is exact as the one provided or returns an empty string if not found
 */
fun Any.firstPropertyValue(equalsToFieldName: String): String {
    return try {
        javaClass.kotlin.memberProperties.first { member ->
            member.name.toLowerCase() == equalsToFieldName.toLowerCase()
        }.get(this).toString()
    } catch (e: java.lang.Exception) {
        ""
    }

}


/**
 * Finds property if the name is exact as the one provided
 */
fun Any.fieldPropertyValue(equalsToFieldName: String): Any? {
    return tryOrNull {
        javaClass.kotlin.memberProperties.first { member ->
            member.name.toLowerCase() == equalsToFieldName.toLowerCase()
        }.get(this)
    }
}


inline fun <reified T : Any> T.getGenericTypeName(fieldName: String): String? {
    return tryOrNull {
        this::class.java.getField(fieldName).genericType.typeName
    }
}

inline fun <reified T : Any> T.getClasstype(fieldName: String): Class<out Any>? {
    return tryOrNull {
        this::class.java.getField(fieldName).type
    }
}


inline fun <reified T : Any> T.getFieldValueByFieldName(fieldName: String): Any? {
    return tryOrNull {
        this::class.java.getField(fieldName).get(this)
        }
}


inline fun <reified T : Any> T.getFieldValueBoolean(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).getBoolean(this)
        }
}


inline fun <reified T : Any> T.getFieldValueByte(fieldName: String): Byte? {
    return tryOrNull {
        this::class.java.getField(fieldName).getByte(this)
    }
}

inline fun <reified T : Any> T.getFieldValueChar(fieldName: String): Char? {
    return tryOrNull {
        this::class.java.getField(fieldName).getChar(this)
    }
}

inline fun <reified T : Any> T.getFieldValueDouble(fieldName: String): Double? {
    return tryOrNull {
        this::class.java.getField(fieldName).getDouble(this)
    }
}

inline fun <reified T : Any> T.getFieldValueFloat(fieldName: String): Float? {
    return tryOrNull {
        this::class.java.getField(fieldName).getFloat(this)
    }
}

inline fun <reified T : Any> T.getFieldValueInt(fieldName: String): Int? {
    return tryOrNull {
        this::class.java.getField(fieldName).getInt(this)
    }
}

inline fun <reified T : Any> T.getFieldValueLong(fieldName: String): Long? {
    return tryOrNull {
        this::class.java.getField(fieldName).getLong(this)
    }
}

inline fun <reified T : Any> T.getFieldValueShort(fieldName: String): Short? {
    return tryOrNull {
        this::class.java.getField(fieldName).getShort(this)
    }
}

inline fun <reified T : Any> T.getFieldModifiers(fieldName: String): Int? {
    return tryOrNull {
        this::class.java.getField(fieldName).modifiers
    }
}

inline fun <reified T : Any> T.isFieldEnumConstant(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).isEnumConstant
    }
}

inline fun <reified T : Any> T.isFieldSynthetic(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).isSynthetic
    }
}

inline fun <reified T : Any> T.fieldToGenericString(fieldName: String): String? {
    return tryOrNull {
        this::class.java.getField(fieldName).toGenericString()
    }
}

inline fun <reified T : Any> T.fieldDeclaredAnnotations(fieldName: String): Array<out Annotation>? {
    return tryOrNull {
        this::class.java.getField(fieldName).declaredAnnotations
    }
}

inline fun <reified T : Any> T.isFieldAccessible(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).isAccessible
    }
}

inline fun <reified T : Any> T.setFieldAccessible(fieldName: String) {
    tryOrNull {
        this::class.java.getField(fieldName).isAccessible = true
        }
}


/**
 * Should call setFieldAccessible first
 */
inline fun <reified T : Any> T.setFieldValue(fieldName: String, value: String) {
    tryOrNull {
        this::class.java.getField(fieldName).set(this, value)
        }
}


inline fun <reified T : Any> T.getGenericType(fieldName: String): Type? {
    return tryOrNull {
        this::class.java.getField(fieldName).genericType
    }
}

inline fun <reified T : Any> T.getAnnotations(fieldName: String): List<Annotation>? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.annotations
    }
}

inline fun <reified T : Any> T.getGetter(fieldName: String): KProperty.Getter<Any?>? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.getter
    }
}

inline fun <reified T : Any> T.isConstant(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isConst
    }
}

inline fun <reified T : Any> T.isLateInit(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isLateinit
    }
}

inline fun <reified T : Any> T.isAbstract(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isAbstract
    }
}

inline fun <reified T : Any> T.isFinal(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isFinal
    }
}

inline fun <reified T : Any> T.isOpen(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isOpen
    }
}

inline fun <reified T : Any> T.isSuspend(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isSuspend
    }
}

inline fun <reified T : Any> T.isFieldNull(fieldName: String): Boolean? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.isNull
    }
}

inline fun <reified T : Any> T.getFieldParameters(fieldName: String): List<KParameter>? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.parameters
    }
}

inline fun <reified T : Any> T.getFieldTypeParameters(fieldName: String): List<KTypeParameter>? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.typeParameters
    }
}

/**
 * Returns a parameter representing the `this` instance needed to call this callable,
 * or `null` if this callable is not a member of a class and thus doesn't take such parameter.
 */
inline fun <reified T : Any> T.getFieldInstanceParameter(fieldName: String): KParameter? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.instanceParameter
    }
}

/**
 * Returns a parameter representing the extension receiver instance needed to call this callable,
 * or `null` if this callable is not an extension.
 */
inline fun <reified T : Any> T.getFieldExtensionReceiverParameter(fieldName: String): KParameter? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.extensionReceiverParameter
    }
}

/**
 * Calls this callable with the specified list of arguments and returns the result.
 * Throws an exception if the number of specified arguments is not equal to the size of [arguments],
 * or if their types do not match the types of the parameters.
 */
inline fun <reified T : Any> T.callFieldWithArguments(fieldName: String, vararg arguments: Any) {
    tryOrIgnore {
        this::class.java.getField(fieldName).kotlinProperty?.call(arguments)
    }
}

/**
 * Calls this callable with the specified list of arguments and returns the result.
 * Throws an exception if the number of specified arguments is not equal to the size of [arguments],
 * or if their types do not match the types of the parameters.
 */
inline fun <reified T : Any> T.callFieldWithArguments(fieldName: String, arguments: Map<KParameter, Any?>) {
    tryOrIgnore {
        this::class.java.getField(fieldName).kotlinProperty?.callBy(arguments)
    }
}


/**
 * If the object has hash code returns it else null
 */
inline fun <reified T : Any> T.getObjectHashCode(fieldName: String): Int? {
    return tryOrNull {
        this::class.java.getField(fieldName).kotlinProperty?.hashCode()
    }
}

/**
 * 0-based index of this parameter in the parameter list of its containing callable.
 */
inline fun KParameter?.getParameterIndex(): Int? {
    return this?.index
}


/**
 * `true` if this parameter is optional and can be omitted when making a call via [KCallable.callBy], or `false` otherwise.
 *
 * A parameter is optional in any of the two cases:
 * 1. The default value is provided at the declaration of this parameter.
 * 2. The parameter is declared in a member function and one of the corresponding parameters in the super functions is optional.
 */
inline fun KParameter?.isOptional(): Boolean? {
    return this?.isOptional
}

/**
 * `true` if this parameter is `vararg`.
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/functions.html#variable-number-of-arguments-varargs)
 * for more information.
 */
inline fun KParameter?.isVararg(): Boolean? {
    return this?.isVararg
}


/**
 * Kind of this parameter.
 *   /** Instance required to make a call to the member, or an outer class instance for an inner class constructor. */
INSTANCE,

/** Extension receiver of an extension function or property. */
EXTENSION_RECEIVER,

/** Ordinary named value parameter. */
VALUE,
 */
inline fun KParameter?.getParameterKind(): KParameter.Kind? {
    return this?.kind
}


/**
 * Type of this parameter. For a `vararg` parameter, this is the type of the corresponding array,
 * not the individual element.
 */
inline fun KParameter?.getParameterType(): KType? {
    return this?.type
}

