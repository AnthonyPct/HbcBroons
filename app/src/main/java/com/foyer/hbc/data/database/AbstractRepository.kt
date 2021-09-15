package com.foyer.hbc.data.database

import kotlin.math.absoluteValue

abstract class AbstractRepository {

    ///////////////////////////////////////////////////////////////////////////
    // CONST
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        const val USERS_COLLECTION = "utilisateurs"
        const val CONSUMPTIONS_COLLECTION = "consumptions"
        const val PAYMENT_COLLECTION = "paiement"
        const val CHECKOUT_COLLECTION = "cagnotte"
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    protected fun getNewCheckoutUnPayedAfterUserConsumptions(
        userMoney: Double,
        consumptionsPrice: Double,
        actualUnPayed: Double
    ): Double {
        return when {
            // Example : User with -10€ as balance and order beers
            userMoney <= 0 -> actualUnPayed.plus(consumptionsPrice)
            // Example : User with 10€ as balance and order 12 beers
            userMoney < consumptionsPrice && userMoney > 0 -> actualUnPayed.plus(consumptionsPrice - userMoney)
            // Nothing, unpayed doesn't change, user has credit
            userMoney > consumptionsPrice -> actualUnPayed

            else -> actualUnPayed
        }
    }

    protected fun getNewCheckoutUnPayedAfterPayment(
        userBalance: Double,
        paymentAmount: Double,
        actualUnPayed: Double
    ): Double {
        return when {
            // If user credits its account which was already in positif don't need to update unpayed value
            userBalance > 0 -> actualUnPayed
            // If user credits more than it's actual credit, we remove his balance to unpayed value
            userBalance < 0 && paymentAmount >= userBalance.absoluteValue -> actualUnPayed - (userBalance.absoluteValue)
            // If user credits lets than it' actual credit, we remove th payment to unpayed value
            userBalance < 0 && paymentAmount < userBalance.absoluteValue -> actualUnPayed - paymentAmount

            else -> actualUnPayed
        }
    }
}