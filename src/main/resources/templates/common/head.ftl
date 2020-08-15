<#macro head host="">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="${host}/css/bootstrap.min.css">
        <!-- Blog CSS -->
        <link rel="stylesheet" href="${host}/css/blog.css">

        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="${host}/js/jquery.min.js"></script>
        <script src="${host}/js/popper.min.js"></script>
        <script src="${host}/js/bootstrap.min.js"></script>

        <#nested >
    </head>
</#macro>