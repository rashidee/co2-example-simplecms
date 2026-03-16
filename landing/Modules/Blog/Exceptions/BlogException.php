<?php

namespace Modules\Blog\Exceptions;

use App\Exceptions\WebApplicationException;

class BlogException extends WebApplicationException
{
    public static function postNotFound(string $slug): self
    {
        return new self("Blog post not found: {$slug}", 404, 'The blog post you are looking for does not exist.');
    }

    public function render(): \Illuminate\Http\Response
    {
        abort($this->getCode());
    }
}
