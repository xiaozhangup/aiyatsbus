package cc.polarastrum.aiyatsbus.core.util

/**
 * 键值对工具类
 * 
 * 提供简单的键值对数据结构，用于兼容 Java 代码。
 * 包含数据类和便捷的创建函数。
 * 避免与 Kotlin 标准库的 Pair 类冲突。
 *
 * @author mical
 * @since 2025/6/20 23:42
 */

/**
 * 键值对数据类
 *
 * 提供简单的键值对数据结构，类似于 Kotlin 标准库的 Pair，
 * 但使用不同的名称以避免冲突。
 * 支持任意类型的键和值。
 *
 * @param A 第一个值的类型
 * @param B 第二个值的类型
 * @param first 第一个值
 * @param second 第二个值
 * 
 * @example
 * ```kotlin
 * // 基本用法
 * val pair = Pair1("name", "value")
 * println(pair.first) // 输出: name
 * println(pair.second) // 输出: value
 * 
 * // 在函数中使用
 * fun getUserInfo(): Pair1<String, Int> {
 *     return Pair1("Alice", 25)
 * }
 * 
 * // 解构声明
 * val (name, age) = getUserInfo()
 * println("$name is $age years old")
 * 
 * // 在集合中使用
 * val pairs = listOf(
 *     Pair1("apple", 1),
 *     Pair1("banana", 2),
 *     Pair1("orange", 3)
 * )
 * 
 * // 转换为 Map
 * val map = pairs.toMap()
 * ```
 */
data class Pair1<A, B>(val first: A, val second: B)

/**
 * 创建键值对的便捷函数
 *
 * 使用中缀语法创建 Pair1 对象，提供更直观的语法。
 * 类似于 Kotlin 标准库的 `to` 函数，但返回 Pair1 而不是 Pair。
 *
 * @param A 第一个值的类型
 * @param B 第二个值的类型
 * @param that 第二个值
 * @return 包含两个值的 Pair1 对象
 * 
 * @example
 * ```kotlin
 * // 基本用法
 * val pair = "key" to1 "value"
 * // 结果: Pair1("key", "value")
 * 
 * // 在配置中使用
 * val config = mapOf(
 *     "server" to1 "localhost",
 *     "port" to1 25565,
 *     "maxPlayers" to1 20
 * )
 * 
 * // 在函数参数中使用
 * fun processData(data: List<Pair1<String, Any>>) {
 *     data.forEach { (key, value) ->
 *         println("$key: $value")
 *     }
 * }
 * 
 * // 调用函数
 * processData(listOf(
 *     "name" to1 "Alice",
 *     "age" to1 25,
 *     "city" to1 "New York"
 * ))
 * 
 * // 在事件处理中使用
 * fun handleEvent(event: String, data: Any) {
 *     val eventData = event to1 data
 *     // 处理事件数据
 * }
 * ```
 */
infix fun <A, B> A.to1(that: B): Pair1<A, B> = Pair1(this, that)