<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{{ $title ?? 'Error' }} - Simple CMS</title>
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="font-sans text-text-primary bg-page-bg min-h-screen flex items-center justify-center">
    <div class="text-center">
        <h1 class="text-6xl font-bold text-primary mb-4">{{ $code ?? '500' }}</h1>
        <p class="text-xl text-text-secondary mb-8">{{ $message ?? 'Something went wrong.' }}</p>
        <a href="/" class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
            Back to Home
        </a>
    </div>
</body>
</html>
