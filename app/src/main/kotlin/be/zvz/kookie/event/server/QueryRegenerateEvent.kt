package be.zvz.kookie.event.server

import be.zvz.kookie.network.query.QueryInfo

class QueryRegenerateEvent(val queryInfo: QueryInfo) : ServerEvent()
