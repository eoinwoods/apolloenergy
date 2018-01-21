package com.artechra.apollo.traces

import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class MySqlZipkinTraceManagerImpl(val jdbcConn : JdbcTemplate) : TraceManager {
    data class SpanItem(val trace_id: String, val start_time_msec : Long, val name : String, val duration_msec : Long)

    val ROOT_SPANS_QUERY = "SELECT hex(trace_id) as trace_id, start_ts/1000 as start_time_msec, name, duration " +
                           "FROM zipkin_spans WHERE trace_id = id"

    override fun getTraces(): List<Trace> {
        val ret : MutableList<Trace> = ArrayList<Trace>()

        for (traceId in getRootSpans()) {
            val trace = getTrace(traceId)
            ret.add(trace)
        }
        return ret
    }

    override fun getTrace(traceId: String): Trace {
        val spans = getSpansForTrace(traceId)
        val trace = Trace(spans.toSet())
        return trace
    }


    fun getRootSpans() : List<String> {
        val ret : MutableList<String> = ArrayList()
        jdbcConn.query(ROOT_SPANS_QUERY) {
            rs: ResultSet, _ : Int -> ret.add(rs.getString("trace_id"))
        }
        return ret
    }

    fun getSpansForTrace(traceId : String) : List<Span> {
        return listOf()
    }




}