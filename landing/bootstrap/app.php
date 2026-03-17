<?php

use App\Exceptions\WebApplicationException;
use Illuminate\Foundation\Application;
use Illuminate\Foundation\Configuration\Exceptions;
use Illuminate\Foundation\Configuration\Middleware;

return Application::configure(basePath: dirname(__DIR__))
    ->withRouting(
        web: __DIR__.'/../routes/web.php',
        commands: __DIR__.'/../routes/console.php',
        health: '/up',
    )
    ->withMiddleware(function (Middleware $middleware): void {
        $middleware->trustProxies(at: '*');
        $middleware->web(append: [
            \App\Http\Middleware\CorrelationIdMiddleware::class,
        ]);
    })
    ->withExceptions(function (Exceptions $exceptions): void {
        $exceptions->render(function (Throwable $e, $request) {
            if ($request->header('HX-Request')) {
                $statusCode = method_exists($e, 'getStatusCode') ? $e->getStatusCode() : 500;
                return response('', $statusCode)
                    ->header('HX-Trigger', json_encode([
                        'showToast' => [
                            'type' => 'error',
                            'message' => $e instanceof WebApplicationException
                                ? ($e->userMessage ?? $e->getMessage())
                                : 'An unexpected error occurred.',
                        ],
                    ]));
            }
        });
    })->create();
