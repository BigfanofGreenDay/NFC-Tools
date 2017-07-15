package cc.metapro.nfc.model

data class Card(val id: Int,
                var title: String,
                var descp: String,
                val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
