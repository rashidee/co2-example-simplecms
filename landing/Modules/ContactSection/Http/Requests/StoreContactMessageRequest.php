<?php

namespace Modules\ContactSection\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreContactMessageRequest extends FormRequest
{
    public function authorize(): bool { return true; }

    public function rules(): array
    {
        return [
            'sender_name' => ['required', 'string', 'max:255'],
            'sender_email' => ['required', 'email', 'max:255'],
            'message_content' => ['required', 'string', 'max:500'],
        ];
    }

    public function messages(): array
    {
        return [
            'message_content.max' => 'Your message must not exceed 500 characters.',
        ];
    }
}
