package webcrawling;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CarRentalWebCrawl {
    public static String carrentalUrl = "https://www.carrentals.com/";

    static ChromeOptions chromeOptions = new ChromeOptions();
//    static EdgeOptions edgeOptions = new EdgeOptions();
    //        chromeOptions.addArguments("--headless");
    static WebDriver driver;
    static WebDriverWait wait;

    public static void initDriver() {

//        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
//        driver = new EdgeDriver(edgeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get(carrentalUrl);
        driver.navigate().refresh();
//        try {
//            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        } catch (Exception e) {
////            System.out.println(CarRentalWebCrawl.class);
//            // If the button is not present after 3 seconds, do nothing
////            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
//        }
    }

    public static void main(String[] args) {
        //driver.get("https://www.orbitz.com/Cars");
        driver.navigate().refresh();
        try {
            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
//            System.out.println(CarRentalWebCrawl.class);
            // If the button is not present after 3 seconds, do nothing
//            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
        }


        System.out.println("Enter your city:");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

//        fetchDeals(userInput);

        // Close the browser
//        driver.quit();
    }

    public static void fetchData(String city, String pickUpDate, String returnDate, String pickUpTime, String returnTime) {
//        driver.get("https://www.orbitz.com/Cars");
//        driver.navigate().refresh();
//        try {
//            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        } catch (Exception e) {
//            // If the button is not present after 3 seconds, do nothing
//            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
//        }


//        System.out.println("Enter your city:");
//        Scanner scanner = new Scanner(System.in);
//        String userInput = scanner.nextLine();

//        fetchDeals(city, pickUpDate,returnDate, pickUpTime, returnTime);
    }

    // Method to navigate to the target month based on user input date
    private static void navigateToTargetMonth(WebDriver driver, String pickupDate, String returnDate) {
        // Split user input date into day, month, and year
        String[] pickupDateParts = pickupDate.split("/");
        String pickDay = pickupDateParts[0];
        String pickMonth = pickupDateParts[1];
        String pickYear = pickupDateParts[2];

        String[] returnDateParts = returnDate.split("/");
        String retDay = returnDateParts[0];
        String retMonth = returnDateParts[1];
        String retYear = returnDateParts[2];

        // Introduce an explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

//        String buttonXPath = String.format("//button[@aria-label='%s %d, %d']", getMonthName(Integer.parseInt(targetMonth)), targetDay, targetYear);
//        WebElement startDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
//        startDateButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("d1-btn"))).click();
        // Wait for the pagination container to be present
        WebElement paginationContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uitk-date-picker-menu-pagination-container")));

        // Locate the buttons for previous and next month
//        WebElement prevMonthButton = paginationContainer.findElement(By.xpath(".//button[contains(@aria-label, 'Previous month')]"));
        List<WebElement> bothButtons = paginationContainer.findElements(By.cssSelector("button[data-stid='date-picker-paging']"));
//        WebElement nextMonthButton = paginationContainer.findElement(By.xpath(".//button[contains(@aria-label, 'Next month')]"));

        // Get the current month and year from the page
        WebElement currentMonthElement = driver.findElement(By.cssSelector(".uitk-date-picker-month-name"));
        System.out.println(currentMonthElement.getText());

        boolean isPickupSelected = false;
        boolean isReturnSelected = false;
        // Continue navigating until the target month and year are reached
        while (getMonthNumber(currentMonthElement.getText().split(" ")[0]) != (Integer.parseInt(pickMonth)) || !isPickupSelected) {
            // Click the appropriate button based on whether the target month is before or after the current month
            if (getMonthNumber(currentMonthElement.getText().split(" ")[0]) == (Integer.parseInt(pickMonth))){
                String buttonXPath = String.format("//button[@aria-label='%s %d, %d']", getMonthName(Integer.parseInt(pickMonth)), Integer.parseInt(pickDay), Integer.parseInt(pickYear));
                WebElement startDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
                startDateButton.click();
                isPickupSelected = true;
            }else {
                if (getMonthNumber(currentMonthElement.getText().split(" ")[0]) > (Integer.parseInt(pickMonth))) {
                    bothButtons.get(0).click();
                } else {
                    bothButtons.get(1).click();
                }
            }
            // Wait for a short duration to allow the calendar to update
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update the current month
            currentMonthElement = driver.findElement(By.cssSelector(".uitk-date-picker-month-name"));
        }


        while (getMonthNumber(currentMonthElement.getText().split(" ")[0]) != (Integer.parseInt(retMonth)) || !isReturnSelected) {
            // Click the appropriate button based on whether the target month is before or after the current month
            if (getMonthNumber(currentMonthElement.getText().split(" ")[0]) == (Integer.parseInt(retMonth))){
                String endDatebuttonXPath = String.format("//button[contains(@aria-label, '%s %d, %d')]", getMonthName(Integer.parseInt(retMonth)), Integer.parseInt(retDay), Integer.parseInt(retYear));
                WebElement endDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(endDatebuttonXPath)));
                endDateButton.click();
                isReturnSelected = true;
            }else {
                if (getMonthNumber(currentMonthElement.getText().split(" ")[0]) > (Integer.parseInt(retMonth))) {
                    bothButtons.get(0).click();
                } else {
                    bothButtons.get(1).click();
                }
            }
            // Wait for a short duration to allow the calendar to update
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update the current month
            currentMonthElement = driver.findElement(By.cssSelector(".uitk-date-picker-month-name"));
        }
    }

    public static void resolveDate(String pickUpDate, String returnDate) {

//        System.out.println("Please enter your pick up date: ");

        // Split the date into day, month, and year
        String[] parts = pickUpDate.split("/");
        int pickday = Integer.parseInt(parts[0]);
        int pickmonth = Integer.parseInt(parts[1]);
        int pickyear = Integer.parseInt(parts[2]);

        String[] eparts = returnDate.split("/");
        int retday = Integer.parseInt(eparts[0]);
        int retmonth = Integer.parseInt(eparts[1]);
        int retyear = Integer.parseInt(eparts[2]);

        navigateToTargetMonth(driver, pickUpDate, returnDate);

        try {
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("d1-btn"))).click();
//            System.out.println("button xpath:"+buttonXPath);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Dec 10, 2023']")));
//            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Dec 10, 2023']")));
//            button.click();

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonXPath)));
//            String buttonXPath = String.format("//button[@aria-label='%s %d, %d']", getMonthName(pickmonth), pickday, pickyear);
//            WebElement startDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
//            startDateButton.click();
//            String endDatebuttonXPath = String.format("//button[contains(@aria-label, '%s %d, %d')]", getMonthName(retmonth), retday, retyear);
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(endDatebuttonXPath)));
//            WebElement endDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(endDatebuttonXPath)));
//            endDateButton.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-stid='apply-date-picker'][aria-label='Save changes and close the date picker.']")));
            // Find the element using a CSS selector
            WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-stid='apply-date-picker'][aria-label='Save changes and close the date picker.']")));
            doneButton.click();

        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Element not found: " + e.getMessage());
        }
    }
    public static void resolveTime(String pickUpTime, String returnTime) {
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[2]/div[2]/div/select"))).click();
//        System.out.println("Enter Pickup Time (HH:MM-AM/PM)");
        String pickupTime = pickUpTime; // Change this variable to the time you want to select
//        System.out.println("Enter Drop off Time (HH:MM-AM/PM)");
        String dropOffTime = returnTime; // Change this variable to the time you want to select

        selectTime(pickupTime, ".uitk-field-select"); // Replace ".pickup-time-select" with the selector for pickup time
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[2]/div[3]/div/select"))).click();

        selectTime(dropOffTime, ".uitk-field-select[aria-label='Drop-off time']"); // Replace ".drop-off-time-select" with the selector for drop-off time

        WebElement searchButton = driver.findElement(By.cssSelector("button[data-testid='submit-button']"));
        searchButton.click();

        try {
            Thread.sleep(12);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-testid='car-offer-card']")));
        WebCrawler.createFile(carrentalUrl, driver.getPageSource(), "orbitz_deals", "CarRentalFiles/");
    }

    public static void handlePickUpLocation(String pickUpLoc) {
        driver.manage().window().maximize();
        wait.until(drive -> ((JavascriptExecutor) drive).executeScript("return document.readyState").equals("complete"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/button"))).click();
        WebElement inputField = driver.findElement(By.xpath("//*[@id=\"location-field-locn\"]"));
        inputField.sendKeys(pickUpLoc);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(" ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(Keys.BACK_SPACE);

        inputField.sendKeys(pickUpLoc);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(" ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(Keys.BACK_SPACE);

//        inputField.sendKeys(pickUpLoc);
//        inputField.sendKeys(Keys.RETURN);

//        try {
//            Thread.sleep(2);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        inputField.sendKeys(" ");
//        driver.navigate().refresh();
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/button"))).click();
//        WebElement inputField1 = driver.findElement(By.xpath("//*[@id=\"location-field-locn\"]"));
//        inputField1.sendKeys(pickUpLoc);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement locationResults = driver.findElement(By.cssSelector("ul[data-stid='location-field-locn-results']"));

        // Find all list items containing location suggestions within the parent element
        List<WebElement> locationItems = locationResults.findElements(By.cssSelector("li[data-stid='location-field-locn-result-item']"));
        int a = 1;
        // Loop through each list item and extract the location suggestion
//        System.out.println("Location Suggestions:");
        for (WebElement locationItem : locationItems) {
            // Find the button element within each list item
            WebElement buttonElement = locationItem.findElement(By.cssSelector("button[data-stid='location-field-locn-result-item-button']"));

            // Get the aria-label attribute containing the location suggestion
            String locationSuggestion = buttonElement.getAttribute("aria-label");

            // Print each location suggestion
//            System.out.println(a + ". " + locationSuggestion);
            a++;
        }
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Select your location: ");
        String userInput = "1";

        List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button[data-stid='location-field-locn-result-item-button']")));

        int indexToClick = Integer.parseInt(userInput); // Change the index based on your requirement (0-based index)

        // Check if the index is valid
        if (indexToClick >= 0 && indexToClick < buttons.size()) {
            // Click the button at the specified index
//            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(buttons.get(indexToClick)));
            buttons.get(indexToClick).click();

        } else {
            System.out.println("Invalid index or element not found.");
        }
    }
    public static void handleDropOffLocation(String dropOffLoc) {
        driver.manage().window().maximize();
        wait.until(drive -> ((JavascriptExecutor) drive).executeScript("return document.readyState").equals("complete"));

//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[1]/div[2]/div/div/div/div/div[2]/div[1]/button"))).click();
//        WebElement inputField = driver.findElement(By.xpath("//*[@id=\"location-field-loc2\"]"));
//        inputField.sendKeys(dropOffLoc);
//        driver.navigate().refresh();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[2]/div/div/div/div/div[2]/div[1]/button"))).click();
        WebElement inputField1 = driver.findElement(By.xpath("//*[@id=\"location-field-loc2\"]"));
        inputField1.sendKeys(dropOffLoc);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement locationResults = driver.findElement(By.cssSelector("ul[data-stid='location-field-loc2-results']"));

        // Find all list items containing location suggestions within the parent element
        List<WebElement> locationItems = locationResults.findElements(By.cssSelector("li[data-stid='location-field-loc2-result-item']"));
        int a = 1;
        // Loop through each list item and extract the location suggestion
//        System.out.println("Location Suggestions:");
        for (WebElement locationItem : locationItems) {
            // Find the button element within each list item
            WebElement buttonElement = locationItem.findElement(By.cssSelector("button[data-stid='location-field-loc2-result-item-button']"));

            // Get the aria-label attribute containing the location suggestion
            String locationSuggestion = buttonElement.getAttribute("aria-label");

            // Print each location suggestion
//            System.out.println(a + ". " + locationSuggestion);
            a++;
        }
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Select your location: ");
        String userInput = "0";
        List<WebElement> buttons = driver.findElements(By.cssSelector("ul[data-stid='location-field-loc2-results'] button[data-stid='location-field-loc2-result-item-button']"));

        int indexToClick = Integer.parseInt(userInput); // Change the index based on your requirement (0-based index)

        // Check if the index is valid
        if (indexToClick >= 0 && indexToClick < buttons.size()) {
            // Click the button at the specified index
            buttons.get(indexToClick).click();

        } else {
            System.out.println("Invalid index or element not found.");
        }
    }

//    private static Hashtable<String, String> fetchDeals(String inputQuery, String pickUpDate, String returnDate, String pickUpTime, String returnTime) {
//
//    }

    public static void selectTime(String inputTime, String selectElementSelector) {
        // Extract hour and minute from the input time
        String[] splitTime = inputTime.split(":");
        int hour = Integer.parseInt(splitTime[0]);
        int minute = Integer.parseInt(splitTime[1].substring(0, 2));
        boolean isPM = splitTime[1].substring(2).trim().equals("PM");
        
        //System.out.println(isPM);
        int timeInMinutes = hour * 60 + minute;
        if(isPM)
        {
        	  timeInMinutes = hour * 60 + minute+720;
        }
        // Find the select element for time
        WebElement timeSelect = driver.findElement(By.cssSelector(selectElementSelector));

        // Loop through options and select the closest time
        for (WebElement option : timeSelect.findElements(By.tagName("option"))) {
            int optionTime = Integer.parseInt(option.getAttribute("data-time"));
            if (optionTime >= timeInMinutes) {
                option.click();
                break;
            }
        }
    }

    private static String getMonthName(int monthNumber) {
        String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return months[monthNumber - 1];
    }

    private static Integer getMonthNumber(String monthName) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("January",1);
        hashMap.put("February",2);
        hashMap.put("March",3);
        hashMap.put("April",4);
        hashMap.put("May",5);
        hashMap.put("June",6);
        hashMap.put("July",7);
        hashMap.put("August",8);
        hashMap.put("September",9);
        hashMap.put("October",10);
        hashMap.put("November",11);
        hashMap.put("December",12);

        return hashMap.get(monthName);
    }

    public static void resetDriver(){
        driver.get(carrentalUrl);
    }

    public static void closeDriver(){
        driver.quit();
    }
}