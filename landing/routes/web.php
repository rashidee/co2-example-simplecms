<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Landing Page Routes
|--------------------------------------------------------------------------
| Module-specific routes are registered via nwidart/laravel-modules
| in each module's routes/web.php file. The routes below are
| application-level routes.
|--------------------------------------------------------------------------
*/

// Home page (landing page with all sections)
Route::get('/', [\App\Http\Controllers\HomeController::class, 'index'])->name('home');
