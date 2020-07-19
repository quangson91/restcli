package uos.dev.restcli.parser

data class Request(
    val method: RequestMethod = RequestMethod.GET,
    val requestTarget: String,
    val httpVersion: String,
    val headers: Map<String, String>,
    val comments: String = "",
    val body: String? = null,
    val fileLoad: String? = null,
    val parts: List<Part> = mutableListOf()
) {
    data class Part(
        val name: String,
        val headers: Map<String, String>,
        val body: String? = null,
        val fileLoad: String? = null
    ) {
        data class Builder @JvmOverloads constructor(
            var name: String = "",
            val headers: MutableMap<String, String> = mutableMapOf(),
            val rawBody: MutableList<String> = mutableListOf()
        ) {
            fun build(): Part = Part(name, headers, rawBody.joinToString(""))
        }
    }

    data class Builder @JvmOverloads constructor(
        var method: RequestMethod = RequestMethod.GET,
        var requestTarget: String? = null,
        var httpVersion: String = DEFAULT_HTTP_VERSION,
        val headers: MutableMap<String, String> = mutableMapOf(),
        val comments: MutableList<String> = mutableListOf(),
        val rawBody: MutableList<String> = mutableListOf(),
        val rawResponseHandler: MutableList<String> = mutableListOf(),
        var rawResponseReference: String? = null,
        val parts: MutableList<Part.Builder> = mutableListOf()
    ) {
        private val isValid: Boolean
            get() = requestTarget?.isNotBlank() ?: false

        fun build(): Request? {
            if (!isValid) {
                return null
            }
            return Request(
                method = method,
                requestTarget = requestTarget.orEmpty(),
                httpVersion = httpVersion,
                headers = headers,
                comments = comments.joinToString("\n"),
                body = rawBody.joinToString("\n"),
                parts = parts.map { it.build() }
            )
        }
    }

    companion object {
        const val DEFAULT_HTTP_VERSION: String = "HTTP/1.1"
    }
}