param([string]$ModelPath)
$ErrorActionPreference='Stop'
$projectRoot=[IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$modelFolder=Join-Path $projectRoot 'app\src\main\assets\models'
$destination=Join-Path $modelFolder 'gemma3-1b-it-int4.litertlm'
$expectedHash='1325ae366d31950f137c9c357b9fa89448b176d76998180c08ceaca78bba98be'
New-Item -ItemType Directory -Path $modelFolder -Force | Out-Null
if($ModelPath) {
    Copy-Item -LiteralPath (Resolve-Path -LiteralPath $ModelPath).Path -Destination $destination
} else {
    $apkPath=Join-Path $modelFolder 'andrea-model-source.apk'
    try {
        Invoke-WebRequest 'https://github.com/Andrenzz/Notsyuu_AI/releases/download/v0.2.0-preview/Andrea-AI-0.2.0-preview.apk' -OutFile $apkPath
        Add-Type -AssemblyName System.IO.Compression.FileSystem
        $archive=[IO.Compression.ZipFile]::OpenRead($apkPath)
        try { $entry=$archive.GetEntry('assets/models/gemma3-1b-it-int4.litertlm');if($null -eq $entry){throw 'Bundled model not found in APK'};[IO.Compression.ZipFileExtensions]::ExtractToFile($entry,$destination,$true) } finally { $archive.Dispose() }
    } finally { if(Test-Path -LiteralPath $apkPath){Remove-Item -LiteralPath $apkPath} }
}
$actual=(Get-FileHash -LiteralPath $destination -Algorithm SHA256).Hash.ToLowerInvariant()
if($actual -ne $expectedHash){throw 'Model checksum mismatch. Use the original model or download the APK again.'}
Write-Output 'Gemma model ready. You can now build Andrea AI.'
