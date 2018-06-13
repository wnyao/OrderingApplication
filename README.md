# ordering_application
![screenshot](https://github.com/wnyao/ordering_application/blob/master/screenshots/screenshot.jpg)

## Description
Ordering application is a mock up designed based to requirements specified at the below section. Few design elements on this mock up are not entirely build as the requirements described, but closely related.

## How-to-run

**Method 1**
1. Open Android Studio 
2. Check out project from version control using Github > https://github.com/wnyao/ordering_application.git
3. Run on emulator with SdkVersion 26 and above

**Method 2**
1. Download project repository from Github > https://github.com/wnyao/ordering_application.git
2. Open Android Studio
3. Open an existing Android Studio Project
3. Run on emulator with target API 26 and above


**Note:** Emulator on Android Studio may notifies that 'System UI isn't responding' due to slow startup and requests to close or wait for the aplication. Either wait or rerun the application after startup of emulator is complete will resolve this issue.

## Fulfilled Requirements
* List of mock menu items with item picture, name, and price display on landing activity.
* Info of menu item, with ability to add to cart and quatity selector (by clicking on the amount number at info page)
* Cart page which list the items added to cart
* Swipe to remove for cart item
* Data persistence through app restarts and jumping between activity
* Give responses to client on remove item, reset cart list, submit order, and add to cart list.

## Requirements
Ordering application is a mock up based upon below requirements:
* **Page 1:** List of mock menu items with item picture, price, short description, and ability to add item to Cart with quantity selector.
* **Page 2:** Cart page which lists the items previously added. User should be able to remove items from cart, and also submit their Cart as an order. No backend is required for this app, simply assume that the order data will be sent somewhere and clear the cart on the client

Bonus points for:
* Data persistence through app restarts
* Frequent git commits in your project with concise commit messages
* Instructions to clone and run on a local machine (linux/macOS)
* Any backend setup that receives the order data and gives a response to the client

