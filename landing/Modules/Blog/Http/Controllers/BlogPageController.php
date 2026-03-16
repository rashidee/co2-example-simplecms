<?php

namespace Modules\Blog\Http\Controllers;

use Illuminate\Http\Request;
use Modules\Blog\Contracts\BlogServiceInterface;
use Modules\Blog\ViewModels\BlogDetailViewModel;
use Modules\Blog\ViewModels\BlogListViewModel;

class BlogPageController
{
    public function __construct(
        private readonly BlogServiceInterface $service,
    ) {}

    public function index(Request $request)
    {
        $page = (int) $request->query('page', 1);
        $posts = $this->service->listActivePosts(page: $page);

        return (new BlogListViewModel($posts))->view('blog::pages.index');
    }

    public function show(string $slug)
    {
        $post = $this->service->getBySlug($slug);

        return (new BlogDetailViewModel($post))->view('blog::pages.show');
    }
}
