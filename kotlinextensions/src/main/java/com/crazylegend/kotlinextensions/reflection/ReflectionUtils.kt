package com.crazylegend.kotlinextensions.reflection

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Constructor
import kotlin.reflect.full.memberProperties


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
        javaClass.kotlin.memberProperties.first { member->
            member.name.toLowerCase() == equalsToFieldName.toLowerCase()
        }.get(this).toString()
    }catch (e:java.lang.Exception){
        ""
    }

}