<?php

namespace Modules\Blog\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Modules\Blog\Services\BlogService;

class BlogController extends Controller
{
    public function __construct(
        private readonly BlogService $blogService,
    ) {}

    public function directory(Request $request)
    {
        $posts = $this->blogService->getActivePosts(10);

        if ($request->header('HX-Request')) {
            return view('blog::partials.blog-list', compact('posts'));
        }

        return view('blog::directory', compact('posts'));
    }

    public function detail(string $slug)
    {
        $post = $this->blogService->getPostBySlug($slug);

        if (!$post) {
            abort(404);
        }

        return view('blog::detail', compact('post'));
    }
}
