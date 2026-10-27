# Usa a imagem oficial do Node.js (versão 20, compatível com Angular mais recente)
FROM node:20-alpine

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de dependências primeiro (ajuda no cache do Docker)
COPY package*.json ./

# Instala as dependências do projeto e o Angular CLI
RUN npm install
RUN npm install -g @angular/cli

# Copia o restante do código do projeto
COPY . .

# Expõe a porta 4200
EXPOSE 4200

# Inicia o servidor do Angular liberando acesso externo
CMD ["ng", "serve", "--host", "0.0.0.0", "--disable-host-check"]