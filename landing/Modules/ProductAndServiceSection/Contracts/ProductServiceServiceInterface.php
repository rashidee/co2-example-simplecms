<?php

namespace Modules\ProductAndServiceSection\Contracts;

use Illuminate\Support\Collection;

interface ProductServiceServiceInterface
{
    public function getAllVisible(): Collection;
}
