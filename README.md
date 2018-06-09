# ordering_application
OrderingApplication

## Description
Ordering application is a mock up designed as regard to requirements specified below. Few design elements of this mock up are not entirely build as the requirements described, but closely related.

## How-to-run

**Method 1**
1. Open Android Studio 
2. Check out project from version control using Github > https://github.com/wnyao/ordering_application.git
3. Run on emulator with SdkVersion 26 and above

**Method 2**
1. Download project repository from Github > https://github.com/wnyao/ordering_application.git
2. Open Android Studio
3. Open an existing Android Studio Project
3. Run on emulator with SdkVersion 26 and above


**Note:** Emulator on Android Studio may notifies that 'System UI isn't responding' and requests to close the aplication. Several rerun to the application may required in resolving this (Bug fixing still in progress).

## Fulfilled Requirements
* List of mock menu items with item picture, name, and price display on landing activity.
* Info of menu item, with ability to add to cart and quality selector (by clicking on the amount number at info page)
* Cart page which list the items added to cart
* Swipe to remove for cart item (data persistentcy on removing item is still inconsistent)

## Requirements
Ordering application is a mock up with below requirements:
* **Page 1:** List of mock menu items with item picture, price, short description, and ability to add item to Cart with quantity selector.
* **Page 2:** Cart page which lists the items previously added. User should be able to remove items from cart, and also submit their Cart as an order. No backend is required for this app, simply assume that the order data will be sent somewhere and clear the cart on the client

Bonus points for:
* Data persistence through app restarts
* Frequent git commits in your project with concise commit messages
* Instructions to clone and run on a local machine (linux/macOS)
* Any backend setup that receives the order data and gives a response to the client

