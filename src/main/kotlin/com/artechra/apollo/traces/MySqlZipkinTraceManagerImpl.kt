package com.artechra.apollo.traces

import com.artechra.apollo.types.Span
import com.artechra.apollo.types.Trace
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class MySqlZipkinTraceManagerImpl(val jdbcConn : JdbcTemplate) : TraceManager {

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
        if (spans.isEmpty()) {
            throw IllegalArgumentException("No data found for trace ID '${traceId}'")
        }
        return Trace(spans.toSet())
    }


    fun getRootSpans() : List<String> {
        val ret = jdbcConn.query(ROOT_SPANS_QUERY) {
            rs: ResultSet, _ : Int -> rs.getString("trace_id")
        }
        return ret
    }

    fun getSpansForTrace(traceId : String) : List<Span> {

        val querySql = SPANS_FOR_TRACE_QUERY_TEMPLATE.format(traceId)

        val results = jdbcConn.query(querySql) {
            rs : ResultSet, _ : Int -> createSpanFromResultSetItem(rs)
        }

        return results
    }

    fun createSpanFromResultSetItem(rs : ResultSet) : Span {
        val span = Span(rs.getString("trace_id"), rs.getString("span_id"),
                rs.getString("ipv4_address"),
                rs.getLong("start_time_msec"), rs.getLong("end_time_msec"),
                rs.getString("parent_id"))
        return span
    }

    companion object {
        val ROOT_SPANS_QUERY = "SELECT hex(trace_id) as trace_id, start_ts/1000 as start_time_msec, name, duration " +
                "FROM zipkin_spans WHERE trace_id = id"

        val SPANS_FOR_TRACE_QUERY_TEMPLATE =
                "SELECT hex(s.trace_id) as trace_id, hex(s.id) as span_id, hex(s.parent_id) as parent_id, " +
                        "start_ts as start_time_msec, start_ts+duration as end_time_msec, " +
                        "inet_ntoa(endpoint_ipv4 & conv('ffffffff', 16, 10)) as ipv4_address, endpoint_port " +
                        "FROM zipkin_spans s, zipkin_annotations a " +
                        "WHERE HEX(s.trace_id) = upper('%s') " +
                        "AND s.trace_id = a.trace_id " +
                        "AND s.id = a.span_id " +
                        "AND a_key = 'sr' " +
                        "ORDER BY start_ts"

    }



}