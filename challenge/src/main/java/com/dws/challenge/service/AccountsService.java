package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.dto.TransferModal;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }


  public void transferBalances(TransferModal transfer) throws Exception {
    Account fromAccount = accountsRepository.getAccount(transfer.getFromAccountId());

    Account toAccount = accountsRepository.getAccount(transfer.getToAccountId());

    if(fromAccount.getBalance().compareTo(transfer.getBalance()) < 0) {
      throw new Exception("Transfer amount is less than zero");
    }

    fromAccount.setBalance(fromAccount.getBalance().subtract(transfer.getBalance()));
    toAccount.setBalance(toAccount.getBalance().add(transfer.getBalance()));
  }
}
