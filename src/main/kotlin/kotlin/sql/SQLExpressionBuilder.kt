package kotlin.sql

import org.joda.time.DateTime
import java.math.BigDecimal
import kotlin.sql.vendors.*

fun Column<*>.count(): Count {
    return Count(this)
}

fun <T: DateTime?> Expression<T>.date(): Date {
    return Date(this)
}

fun <T: DateTime?> Expression<T>.month(): Month {
    return Month(this)
}

fun Column<*>.countDistinct(): Count {
    return Count(this, true)
}

fun<T> Column<T>.min(): Min<T> {
    return Min(this, this.columnType)
}

fun<T> Column<T>.max(): Max<T> {
    return Max(this, this.columnType)
}

fun<T> Column<T>.sum(): Sum<T> {
    return Sum(this, this.columnType)
}

fun<T:String?> Column<T>.substring(start: Int, length: Int): Substring {
    return Substring(this, LiteralOp(IntegerColumnType(), start), LiteralOp(IntegerColumnType(), length))
}

fun<T:String?> Column<T>.trim(): Trim {
    return Trim(this)
}

fun <T> Column<T>.distinct(): Distinct<T> {
    return Distinct(this, this.columnType)
}

fun <T:Any?> Column<T>.groupConcat(separator: String? = null, distinct: Boolean = false, vararg orderBy: Pair<Expression<*>,Boolean>): GroupConcat {
    return GroupConcat(this, separator, distinct, *orderBy)
}

object SqlExpressionBuilder: DialectSpecificFunctions {
    public fun <T:Any> coalesce(expr: ExpressionWithColumnType<out T?>, alternate: ExpressionWithColumnType<out T>): ExpressionWithColumnType<T> {
        return Coalesce(expr, alternate)
    }

    fun case(value: Expression<*>? = null) : Case {
        return Case(value)
    }

    public fun<T> ExpressionWithColumnType<T>.wrap(value: T): Expression<T> {
        return QueryParameter(value, columnType)
    }

    infix public fun<T> ExpressionWithColumnType<T>.eq(t: T) : Op<Boolean> {
        if (t == null) {
            return isNull()
        }
        return EqOp(this, wrap(t))
    }

    public fun<T, S: T> Expression<T>.eq(other: Expression<S>) : Op<Boolean> {
        return EqOp (this, other)
    }

    infix public fun<T, S: T> ExpressionWithColumnType<T>.eq(other: Expression<S>) : Op<Boolean> {
        return EqOp (this, other)
    }

    infix public fun<T> ExpressionWithColumnType<T>.neq(other: T): Op<Boolean> {
        if (other == null) {
            return isNotNull()
        }

        return NeqOp(this, wrap(other))
    }

    public fun<T, S: T> ExpressionWithColumnType<T>.neq(other: Expression<S>) : Op<Boolean> {
        return NeqOp (this, other)
    }

    public fun<T> ExpressionWithColumnType<T>.isNull(): Op<Boolean> {
        return IsNullOp(this)
    }

    public fun<T> ExpressionWithColumnType<T>.isNotNull(): Op<Boolean> {
        return IsNotNullOp(this)
    }

    infix public fun<T> ExpressionWithColumnType<T>.less(t: T) : Op<Boolean> {
        return LessOp(this, wrap(t))
    }

    public fun<T, S: T> ExpressionWithColumnType<T>.less(other: Expression<S>) : Op<Boolean> {
        return LessOp (this, other)
    }

    infix public fun<T> ExpressionWithColumnType<T>.lessEq(t: T) : Op<Boolean> {
        return LessEqOp(this, wrap(t))
    }

    public fun<T, S: T> ExpressionWithColumnType<T>.lessEq(other: Expression<S>) : Op<Boolean> {
        return LessEqOp(this, other)
    }

    infix public fun<T> ExpressionWithColumnType<T>.greater(t: T) : Op<Boolean> {
        return GreaterOp(this, wrap(t))
    }

    public fun<T, S: T> ExpressionWithColumnType<T>.greater(other: Expression<S>) : Op<Boolean> {
        return GreaterOp (this, other)
    }

    infix public fun<T> ExpressionWithColumnType<T>.greaterEq(t: T) : Op<Boolean> {
        return GreaterEqOp (this, wrap(t))
    }

    public fun<T, S: T> ExpressionWithColumnType<T>.greaterEq(other: Expression<S>) : Op<Boolean> {
        return GreaterEqOp (this, other)
    }

    operator public fun<T, S: T> ExpressionWithColumnType<T>.plus(other: Expression<S>) : ExpressionWithColumnType<T> {
        return PlusOp (this, other, columnType)
    }

    operator public fun<T> ExpressionWithColumnType<T>.plus(t: T) : ExpressionWithColumnType<T> {
        return PlusOp (this, wrap(t), columnType)
    }

    operator public fun<T, S: T> ExpressionWithColumnType<T>.minus(other: Expression<S>) : ExpressionWithColumnType<T> {
        return MinusOp (this, other, columnType)
    }

    operator public fun<T> ExpressionWithColumnType<T>.minus(t: T) : ExpressionWithColumnType<T> {
        return MinusOp (this, wrap(t), columnType)
    }

    operator public fun<T, S: T> ExpressionWithColumnType<T>.times(other: Expression<S>) : ExpressionWithColumnType<T> {
        return TimesOp (this, other, columnType)
    }

    operator public fun<T> ExpressionWithColumnType<T>.times(t: T) : ExpressionWithColumnType<T> {
        return TimesOp (this, wrap(t), columnType)
    }

    operator public fun<T, S: T> ExpressionWithColumnType<T>.div(other: Expression<S>) : ExpressionWithColumnType<T> {
        return DivideOp (this, other, columnType)
    }

    operator public fun<T> ExpressionWithColumnType<T>.div(t: T) : ExpressionWithColumnType<T> {
        return DivideOp (this, wrap(t), columnType)
    }

    infix public fun<T:String?> ExpressionWithColumnType<T>.like(pattern: String): Op<Boolean> {
        return LikeOp(this, QueryParameter(pattern, columnType))
    }

    infix public fun<T:String?> ExpressionWithColumnType<T>.notLike(pattern: String): Op<Boolean> {
        return NotLikeOp(this, QueryParameter(pattern, columnType))
    }

    infix public fun<T:String?> ExpressionWithColumnType<T>.regexp(pattern: String): Op<Boolean> {
        return RegexpOp(this, QueryParameter(pattern, columnType))
    }

    infix public fun<T:String?> ExpressionWithColumnType<T>.notRegexp(pattern: String): Op<Boolean> {
        return NotRegexpOp(this, QueryParameter(pattern, columnType))
    }

    infix public fun<T> ExpressionWithColumnType<T>.inList(list: List<T>): Op<Boolean> {
        return InListOrNotInListOp(this, list, isInList = true)
    }

    infix public fun<T> ExpressionWithColumnType<T>.notInList(list: List<T>): Op<Boolean> {
        return InListOrNotInListOp(this, list, isInList = false)
    }

    public fun<T, S: Any> ExpressionWithColumnType<T>.asLiteral(value: S): LiteralOp<*> {
        return when (value) {
            is Int -> intLiteral(value)
            is Long -> longLiteral(value)
            is String -> stringLiteral(value)
            is DateTime -> dateTimeLiteral(value)
            else -> LiteralOp<T>(columnType, value)
        }
    }

    public fun<T, S: Any> ExpressionWithColumnType<T>.between(from: S, to: S): Op<Boolean> {
        return Between(this, asLiteral(from), asLiteral(to))
    }

    public fun ExpressionWithColumnType<Int>.intToDecimal(): ExpressionWithColumnType<BigDecimal> {
        return NoOpConversion(this, DecimalColumnType(15, 0))
    }

    override fun <T : String?> ExpressionWithColumnType<T>.match(pattern: String, mode: MatchMode?): Op<Boolean> {
        return with((dialect as DialectSpecificFunctions)) {
            this@match.match(pattern, mode)
        }
    }

    infix fun <T : String?> ExpressionWithColumnType<T>.match(pattern: String): Op<Boolean> = match(pattern, null)
}
