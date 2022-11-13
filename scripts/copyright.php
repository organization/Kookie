<?php

$absolutePath = realpath("../app/src/main/kotlin");

$copyright = <<<COPYRIGHT
/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 - 2022 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
COPYRIGHT;
$directoryIterator = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($absolutePath));
/** @var SplFileInfo $fileInfo */
foreach($directoryIterator as $fileInfo){
    if($fileInfo->isFile()){
        $content = file_get_contents($fileInfo->getPathname());
        if(!str_starts_with($content, "/**")){
            $content = $copyright . "\n" . $content;
            file_put_contents($fileInfo->getPathname(), $content);
        }
    }
}
