@extends('layouts.landing')

@section('title', 'Simple CMS - Home')
@section('meta_description', 'Simple CMS - Create and manage your marketing pages and blog content')

@section('content')

    @include('herosection::components.hero-carousel', ['heroes' => $heroes])

    @include('productandservicesection::components.product-service-grid', ['products' => $products])

    @include('featuressection::components.features-grid', ['features' => $features])

    @include('testimonialssection::components.testimonials-carousel', ['testimonials' => $testimonials])

    @include('teamsection::components.team-grid', ['teamMembers' => $teamMembers])

    @include('contactsection::components.contact-section', ['contactInfo' => $contactInfo])

@endsection
