package cc.metapro.nfc.model

enum class CardType {
    ID {
        fun string(): String {
            return "ID CARD"
        }
    },
    BANK {
        fun string(): String {
            return "BANK CARD"
        }
    },
}