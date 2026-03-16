<?php

namespace App\Http\Controllers;

use Illuminate\Http\Response;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Cache;

class ImageController extends Controller
{
    private const ALLOWED_TABLES = [
        'hero-section' => ['table' => 'hrs_hero_section', 'columns' => ['image_data', 'thumbnail_data']],
        'product-service' => ['table' => 'pas_product_service', 'columns' => ['image_data', 'thumbnail_data']],
        'team-member' => ['table' => 'tms_team_member', 'columns' => ['image_data']],
        'blog-post' => ['table' => 'blg_blog_post', 'columns' => ['image_data', 'thumbnail_data']],
    ];

    public function show(string $table, string $id, string $column): Response
    {
        $config = self::ALLOWED_TABLES[$table] ?? null;
        if (!$config || !in_array($column, $config['columns'])) {
            abort(404);
        }

        $cacheKey = "img:{$table}:{$id}:{$column}";
        $data = Cache::remember($cacheKey, 3600, function () use ($config, $id, $column) {
            $row = DB::table($config['table'])
                ->where('id', $id)
                ->first([$column]);

            if (!$row) {
                return null;
            }

            $value = $row->{$column};

            // Handle PostgreSQL bytea stream resource
            if (is_resource($value)) {
                return stream_get_contents($value);
            }

            return $value;
        });

        if (!$data) {
            abort(404);
        }

        $mime = $this->detectMimeType($data);

        return response($data)
            ->header('Content-Type', $mime)
            ->header('Cache-Control', 'public, max-age=86400');
    }

    private function detectMimeType(string $data): string
    {
        $finfo = new \finfo(FILEINFO_MIME_TYPE);
        return $finfo->buffer($data) ?: 'application/octet-stream';
    }
}
