package com.sthoray.allright.utils

import com.sthoray.allright.R

/**
 * The app icon for second tier categories.
 *
 * @property key The AllGoods API app icon key.
 * @property resourceId The ID of the image asset resource to display in the view.
 */
enum class AppIcon(val key: String, val resourceId: Int) {
    PICTURE(
        "picture",
        R.drawable.ic_category_image_24
    ),
    BOOK(
        "book",
        R.drawable.ic_category_book_24
    ),
    BRIEFCASE(
        "briefcase",
        R.drawable.ic_category_work_24
    ),
    CAR(
        "car2",
        R.drawable.ic_category_car_24
    ),
    DEVICES(
        "laptop-phone",
        R.drawable.ic_category_devices_24
    ),
    CUTLERY(
        "fork-knife2",
        R.drawable.ic_category_food_beverage_24
    ),
    HEART(
        "heart2",
        R.drawable.ic_category_heart_24
    ),
    COUCH(
        "couch",
        R.drawable.ic_category_event_seat_24
    ),
    STROLLER(
        "stroller",
        R.drawable.ic_category_stroller_24
    ),
    SHIRT(
        "shirt",
        R.drawable.ic_category_people_24
    ),
    GOWN(
        "gown",
        R.drawable.ic_category_people_24
    ),
    GUITAR(
        "guitar",
        R.drawable.ic_category_speaker_24
    ),
    HAT(
        "witch-hat",
        R.drawable.ic_category_supervised_24
    ),
    PAW(
        "paw",
        R.drawable.ic_category_pets_24
    ),
    SOCCER(
        "soccer",
        R.drawable.ic_category_sports_soccer_24
    ),
    TICKETS(
        "",
        R.drawable.ic_category_ticket_24
    ),
    HAMMER(
        "hammer",
        R.drawable.ic_category_build_24
    ),
    DICE(
        "dice",
        R.drawable.ic_category_toys_24
    ),
    TRAVEL(
        "traveling",
        R.drawable.ic_category_train_24
    ),
    BALLOON(
        "balloon",
        R.drawable.ic_category_giftcard_24
    )
}