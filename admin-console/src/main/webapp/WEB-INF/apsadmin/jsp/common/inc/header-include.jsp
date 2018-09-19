<%@ taglib prefix="wp" uri="/aps-core" %>

<!--CSS inclusions-->
<link rel="stylesheet" href="<wp:resourceURL />administration/bootstrap/css/bootstrap.min.css" media="screen" />
<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly.min.css">
<link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly-additions.min.css">
<link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css">
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/pages/settingsPage.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/bootstrap.vertical-tabs.min.css"/>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/cropper-js/cropper.min.css"/>

<style>

    /*.modal-dialog {*/
        /*width: 100%;*/
        /*height: 100%;*/
        /*margin: 0;*/
        /*padding: 0;*/
    /*}*/

    /*.modal-content {*/
        /*height: auto;*/
        /*min-height: 100%;*/
        /*border-radius: 0;*/
    /*}*/

    /* Limit image width to avoid overflow the container */

    @media (min-width: 992px){
        .modal-xlg {
            width: 1200px;
        }
    }


    .fileUpload_0 {
        max-width: 100%; /* This rule is very important, please do not ignore this! */
        max-height: 100%;
    }

    .bs-cropping-modal ul.image-navigation li:nth-child(even) {
        background-color: #F5F5F5;
    }

    .bs-cropping-modal ul.image-navigation li:nth-child(odd) {
    }

    .bs-cropping-modal ul.image-navigation  li {

    }

    .bs-cropping-modal ul.image-navigation  li a {
        line-height: 34px;
    }

    .bs-cropping-modal ul.image-navigation  li.active {
        background-color: #0088CE;
    }
    .bs-cropping-modal ul.image-navigation  li.active a {
        color: white;
        background-color: transparent;
        border: none;

    }

    @media (min-width: 992px) {
        .bs-cropping-modal ul.image-navigation  li.active a:before {
            content: "";
            width: 0;
            height: 0;
            border-top: 14.5px solid transparent;
            border-bottom: 14.5px solid transparent;
            border-right: 26px solid #0088CE;
            display: block;
            position: absolute;
            left: -21px;
        }
    }


    .bs-cropping-modal ul.image-navigation  li a {
        color: #979797;
        background-color: transparent;
        border: none;
    }
    .bs-cropping-modal ul.image-navigation  li a:hover {
        text-decoration: none;
        font-weight: normal;
        border: none;

    }

    .bs-cropping-modal .toolbar-container .btn-primary {
        background: transparent;
        color: #363636;
        font-size: 20px;
        border: none;
        box-shadow: none;
    }

    .bs-cropping-modal .toolbar-container .btn-primary {

    }

    /*.arrow-left {*/
        /*width: 0;*/
        /*height: 0;*/
        /*border-top: 10px solid transparent;*/
        /*border-bottom: 10px solid transparent;*/

        /*border-right:10px solid blue;*/
    /*}*/


</style>

<style>
    .img-container,
    .img-preview {
        background-color: #f7f7f7;
        text-align: center;
        width: 100%;
    }

    .img-container {
        margin-bottom: 1rem;
        max-height: 497px;
        min-height: 200px;
    }

    @media (min-width: 768px) {
        .img-container {
            min-height: 497px;
        }
    }

    .img-container > img {
        max-width: 100%;
    }

    .docs-preview {
        margin-right: -1rem;
    }

    .img-preview {
        float: left;
        margin-bottom: .5rem;
        margin-right: .5rem;
        overflow: hidden;
    }

    .img-preview > img {
        max-width: 100%;
    }

    .preview-lg {
        width: 200px;
        height: 200px;
        max-width: 100%;
    }

    .preview-md {
        width: 100px;
        height: 100px;
        max-width: 100%;

    }

    .preview-sm {
        width: 70px;
        height: 70px;
        max-width: 100%;
    }

    .preview-xs {
        width: 20px;
        height: 20px;
        margin-right: 0;
        max-width: 100%;
    }



</style>

<!--JS inclusions-->
<script src="<wp:resourceURL />administration/js/jquery-2.2.4.min.js"></script>
<script src="<wp:resourceURL />administration/js/entando-stream.js"></script>
<script src="<wp:resourceURL />administration/js/jquery.matchHeight-min.js"></script>
<script src="<wp:resourceURL />administration/patternfly/js/patternfly.js"></script>
<script src="<wp:resourceURL />administration/js/pages/settingsPage.js"></script>
<script src="<wp:resourceURL />administration/bootstrap/js/bootstrap.min.js"></script>
<script src="<wp:resourceURL />administration/js/bootstrap-switch.min.js"></script>
<script src="<wp:resourceURL />administration/js/bootstrap-datepicker/bootstrap-datepicker.js"></script>
<script src="<wp:resourceURL />administration/cropper-js/cropper.min.js"></script>
