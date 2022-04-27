# CoBudget

Control your budget


![Week view on desktop](https://user-images.githubusercontent.com/50179033/165453462-88aa6445-d024-404a-8481-55efcc85f7f2.png)
<p align="center">
  <img height="500" src="https://user-images.githubusercontent.com/50179033/165453889-b79564e4-4482-4689-a47a-74cde946272b.png">
</p>

## Introduction

This app helps you take control over your budget. You can:

- record your every day expenses and incomes,
- analyze your spending over months and years,
- plan your money budget for the next month,
- get notifications to remind you to save your daily costs

## Technologies

- JDK 17
- Maven
- Spring Boot 2.6.6
- Spring Data JDBC
- PostgreSQL
- HTML
- CSS
- JavaScript
- Vue

## Current features

- Login and sign-up
- Adding expense
- Week view on desktop and mobile
- Add categories and subcategories

## Local usage

You need to install Docker and Docker Compose. In the main directory run `docker-compose up --build` to run application.
You can reach it at `http://localhost:5000`.

## Example of use

### Budget plannning

Every month you write all of your incomes and expense that you are expecting for the next month.
It helps you control if you aren't spend too much money on some categories of products.

### Expenses saving

Every day you quickly add all of your daily expenses. Doing this regularly will take you a few minutes but
will help you a lot in finding the areas that take too much money from you.

### Costs analyzing

After each month you can see a raport that will tell you how good you planned your month and how good you sticked to
this plan.
You can use it to make your month planning better. You can also spot the areas where you can save some money.

## Inspiration

Inspired by budget planning techniques from [Jak oszczędzać pieniądze?](https://jakoszczedzacpieniadze.pl)

## Licence

MIT
