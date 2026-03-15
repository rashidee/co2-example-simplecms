<?php

namespace Modules\ContactSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\ContactSection\Http\Requests\StoreContactMessageRequest;
use Modules\ContactSection\Services\ContactMessageService;

class ContactSectionController extends Controller
{
    public function __construct(
        private readonly ContactMessageService $contactMessageService,
    ) {}

    public function store(StoreContactMessageRequest $request)
    {
        $this->contactMessageService->store($request->validated());

        if ($request->header('HX-Request')) {
            return response('<div class="text-success font-semibold">Thank you for your message! We will get back to you soon.</div>');
        }

        return redirect()->back()->with('success', 'Thank you for your message!');
    }
}
