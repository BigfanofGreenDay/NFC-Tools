package cc.metapro.nfc.model

import com.github.devnied.emvnfccard.model.EmvCard
import com.google.gson.Gson

data class Card(val id: String, var title: String,
                var descp: String, var key: String,
                val tech: String, val data: String) : EmvCard() {

    var emvCard: String = ""

    constructor(id: String, title: String,
                descp: String, key: String,
                tech: String, data: String,
                emvCard: EmvCard?) : this(id, title, descp, key, tech, data) {

        if (emvCard != null) {
            cardNumber = emvCard.cardNumber
            aid = emvCard.aid
            service = emvCard.service
            atrDescription = emvCard.atrDescription
            holderLastname = emvCard.holderLastname
            holderFirstname = emvCard.holderFirstname
            expireDate = emvCard.expireDate
            type = emvCard.type
            leftPinTry = emvCard.leftPinTry
            applicationLabel = emvCard.applicationLabel
            listTransactions = emvCard.listTransactions
            atrDescription = emvCard.atrDescription
            isNfcLocked = emvCard.isNfcLocked
            this.emvCard = Gson().toJson(emvCard)
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
