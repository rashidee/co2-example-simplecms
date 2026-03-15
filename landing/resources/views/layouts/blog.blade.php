<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>@yield('title', 'Blog - Simple CMS')</title>
    <meta name="description" content="@yield('meta_description', 'Read the latest articles from Simple CMS')">
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg">

    @include('partials.navbar', ['activePage' => 'blog'])

    <!-- Spacer for fixed nav -->
    <div class="h-16"></div>

    @yield('content')

    @include('partials.footer')

</body>
</html>
