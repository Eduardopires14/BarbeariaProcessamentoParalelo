# 💈 Problema da Barbearia de Hilzer — Programação Concorrente em Java

## 🧠 Sobre o Projeto
Este projeto implementa a versão clássica do **Problema da Barbearia de Hilzer**, conforme apresentado por *William Stallings (2012)*.  
O objetivo é **praticar programação concorrente** utilizando **Threads em Java** e mecanismos de **sincronização**, simulando o funcionamento de uma barbearia com múltiplos barbeiros e clientes.

---

## 🎯 Objetivos
- Aplicar conceitos de **concorrência e sincronização** em Java.  
- Garantir **execução segura** entre múltiplas threads (sem deadlocks ou starvation).  
- Implementar **disciplina FIFO** para o gerenciamento de clientes.  
- Registrar **logs detalhados** para auditoria dos eventos da simulação.

---

## 🪑 Cenário da Barbearia
A barbearia possui:
- **3 barbeiros** e **3 cadeiras de atendimento**  
- **1 sofá com 4 lugares** na sala de espera  
- **Capacidade total de 20 clientes** dentro da barbearia  
- **1 caixa (POS)** para pagamento, permitindo apenas **um cliente pagando por vez**

---

## 🔄 Regras de Funcionamento
- Nenhum cliente entra se a capacidade total (20) estiver atingida.  
- Se houver lugar no sofá, o cliente se senta; caso contrário, espera em pé.  
- Quando um barbeiro fica livre:
  - O cliente que está há mais tempo no sofá é atendido.  
  - O cliente que está há mais tempo em pé se senta.  
- Qualquer barbeiro pode receber pagamento, mas apenas **um cliente paga por vez**.  
- Barbeiros alternam entre **cortar cabelo**, **receber pagamento** e **dormir** enquanto aguardam novos clientes.

---

## ⚙️ Restrições Técnicas
- ❌ **Proibido busy-wait**  
- 🚫 **Sem deadlocks ou starvation**  
- 📋 **FIFO explícito** no sofá e na promoção de clientes em pé → sofá  
- 🧮 **Capacidade total rigorosa (20)**  
- 💳 **Pagamento serializado**  
- 🧾 **Logs detalhados** para eventos:
  - Entrada na barbearia  
  - Sentar no sofá  
  - Ser chamado para atendimento  
  - Pagamento  
  - Saída  

