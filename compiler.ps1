# Executar gradle clean
Write-Host "Executando gradle clean..."
gradle clean

# Executar gradle build com refresh de dependências
Write-Host "Executando gradle build --refresh-dependencies..."
gradle build --refresh-dependencies

# Pausar no final
Write-Host "Processo concluído. Pressione qualquer tecla para sair."
pause