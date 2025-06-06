<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Concerts</title>
    <link rel="stylesheet" href="./css/site.css">

    <script type="module" src="./js/enable-modals.js"></script>
    <link rel="stylesheet" href="./css/modal.css">

    <script type="module" src="./js/concerts.js"></script>
</head>
<body>

<%@include file="navbar.jsp"%>

<div class="container master-detail-container">

    <div class="master left-sidebar flex-col align-center">
        <h1>Concerts</h1>
        <div id="concerts-list" class="image-list flex-col">

            <!-- Smaller images for every concert will go here. -->

        </div>
    </div>

    <div class="detail">

        <div class="sticky flex-col align-center">
            <h1 id="concert-title"></h1>
            <img id="concert-image" class="shadow" src="" style="width: 750px; height: 250px;">

            <h3>Description</h3>
            <p id="concert-blurb" class="description"></p>

            <div class="align-self-stretch flex-row justify-sb align-stretch">
                <div class="flex-col align-center">
                    <h3>Performers</h3>
                    <div id="concert-performers" class="performers flex-row align-center">

                    </div>
                </div>

                <div class="flex-col align-center">
                    <h3>Dates</h3>
                    <table id="concert-dates" class="dates-table">

                    </table>
                </div>
            </div>
        </div>

    </div>

</div>
<div id="modals-here"></div>
</body>
</html>
