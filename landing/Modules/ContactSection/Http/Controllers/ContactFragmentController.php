<?php

namespace Modules\ContactSection\Http\Controllers;

use Illuminate\Http\Response;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\Http\Requests\StoreContactMessageRequest;

class ContactFragmentController
{
    public function __construct(
        private readonly ContactSectionServiceInterface $service,
    ) {}

    public function store(StoreContactMessageRequest $request): Response
    {
        $this->service->submitMessage($request->validated());

        return response('', 200)
            ->header('HX-Trigger', json_encode([
                'showToast' => [
                    'type' => 'success',
                    'message' => 'Thank you! Your message has been sent successfully.',
                ],
                'resetContactForm' => true,
            ]));
    }
}
