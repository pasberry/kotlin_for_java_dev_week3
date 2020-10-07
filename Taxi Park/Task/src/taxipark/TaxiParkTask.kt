package taxipark

import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {

    val realDrivers = this.trips
        .map (Trip::driver)
        .toSet()

    return allDrivers subtract realDrivers

}


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {

    val faithfulPassengers = if (minTrips == 0) {
        allPassengers
    }
    else {
        this.trips
            .map(Trip::passengers)
            .flatten()
            .groupingBy { it.name }
            .eachCount()
            .filterValues { it >= minTrips }
            .map { Passenger(it.key) }
            .toSet()

    }

    return faithfulPassengers
}


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> = this.trips
        .filter { it.driver == driver }
        .map(Trip::passengers)
        .flatten()
        .groupingBy { it.name }
        .eachCount()
        .filterValues { it > 1 }
        .map {Passenger(it.key)}
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {

    fun getPassengerDiscountedTripsTotal(passenger: Passenger) : Int = this.trips
            .asSequence()
            .filter { it.discount != null }
            .map(Trip::passengers)
            .flatten()
            .filter{it.name == passenger.name }
            .count()

    fun getPassengerNonDiscountedTripsTotal(passenger: Passenger) : Int = this.trips
            .asSequence()
            .filter { it.discount == null }
            .map(Trip::passengers)
            .flatten()
            .filter{it.name == passenger.name }
            .count()

    return this.allPassengers
            .filter { (getPassengerDiscountedTripsTotal(it)
                > getPassengerNonDiscountedTripsTotal(it) )}
            .toSet()

}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {

    fun getIntRangeSegments() = this.trips
            .map(Trip::duration)
            .toList()
            .groupBy { it / 10 }
            .mapKeys { IntRange(it.key * 10, (it.key * 10) + 9) }
            .mapValues { it.value.size }

    var largestRangeTotal = 0;
    var range:IntRange? = null

    val results = getIntRangeSegments()
            .forEach{(key,value) ->
                if (value > largestRangeTotal){
                    range = key
                    largestRangeTotal = value
                }
            }

    return range

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {

    if (trips == null || trips.size == 0) {
        return false
    }

    fun getTotalTripsIncome():Double = trips
            .map(Trip::cost)
            .sum()

    fun getTotalTripsIncomeForDriver(driver: Driver):Double = trips
            .filter { it.driver == driver }
            .map(Trip::cost )
            .sum()

    fun getAllDriversIncome():Set<Double> = allDrivers
            .map { getTotalTripsIncomeForDriver(it) }
            .sortedDescending()
            .toSet()

    val eightyPercentRevenue = 0.8 * getTotalTripsIncome()

    val twentyPercentDriver = (floor(0.2 * allDrivers.count())).toInt()

    val topDriverRevenue = getAllDriversIncome()
            .take(twentyPercentDriver)
            .sum()
    return topDriverRevenue >= eightyPercentRevenue
}