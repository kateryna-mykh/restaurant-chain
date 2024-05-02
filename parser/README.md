# restaurant-chain app

This is console script application that parses a collection of JSON files representing restaurants and generates statistics based on one of their attributes.  
It takes two input parameters: the path to the folder containing the JSON files and the name of the attribute used for generating the statistics. 
This project provides a simple and efficient way to analyze restaurant data and extract meaningful insights.


## How lanch this project

Execute all commands from `parser` project folder.
```
mvn clean package
java -jar target/exec-json-parser.jar [path-to-folder] [atribute-name]
```
All result files writes to `statistics` folder, created in initial [path-to-folder].

## Entities description
For now there is 1 entity Restaurant, that have locationAddress, manager, seetsCapacity, employeesNumber, menuItems, restaurantChain. 
A RestaurantChain have 1 or more Restaurants, but Restaurant assosiated only with one RestaurantChain.

## Input/Output Data files examples
Input file:

```
[
  {
    "locationAddress": "123 Main Street",
    "manager": "John Doe",
    "seetsCapacity": 50,
    "employeesNumber": 10,
    "menuItems": ["Burger", "Pizza", "Salad", "French Fries", "Soft Drink"],
    "restaurantChain": "Delicious Eats"
  },
  {
    "locationAddress": "1516 Maple Drive",
    "manager": "Jessica Brown",
    "seetsCapacity": 60,
    "employeesNumber": 12,
    "menuItems": ["Burger", "Pizza", "Salad", "Cheese Fries", "Lemonade"],
    "restaurantChain": "Delicious Eats"
  }
]
```

Output file "statistics_by_menuItems.xml":

```
<statistics>
  <item>
    <value>Burger</value>
    <count>2</count>
  </item>
  <item>
    <value>Pizza</value>
    <count>2</count>
  </item>
  <item>
    <value>Salad</value>
    <count>2</count>
  </item>
  <item>
    <value>Cheese Fries</value>
    <count>1</count>
  </item>
  <item>
    <value>French Fries</value>
    <count>1</count>
  </item>
  <item>
    <value>Lemonade</value>
    <count>1</count>
  </item>
  <item>
    <value>Soft Drink</value>
    <count>1</count>
  </item>
</statistics>
```

## Results metrics with mulithreding 
Used 10 files (1 file with 1 entity, 3 with 10, 6 with 100):

| Threads  | Execution time (ms)|
| ---------|:------------------:|
| 1        | 844                |
| 2        | 778                |
| 4        | 713                |
| 8        | 755                |

