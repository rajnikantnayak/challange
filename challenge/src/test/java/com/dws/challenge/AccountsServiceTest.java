package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.dto.TransferModal;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  private Account fromAccount;
  private Account toAccount;

  @BeforeEach
  public void setUp(){
    fromAccount = new Account("Id-123");
    fromAccount.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(fromAccount);

    toAccount = new Account("Id-124");
    toAccount.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(toAccount);
  }

  @Test
  void addAccount() {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  void addAccount_failsOnDuplicateId() {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }

  @Test
  void transferBalanceTest() throws Exception {
    TransferModal transferModal = new TransferModal();
    transferModal.setFromAccountId(fromAccount.getAccountId());
    transferModal.setToAccountId(toAccount.getAccountId());
    transferModal.setBalance(new BigDecimal(100));

    accountsService.transferBalances(transferModal);

    assertEquals(new BigDecimal(1100), toAccount.getBalance());
  }

  @Test()
  public void testNegativeAmountTransfer() throws Exception {

    TransferModal transferModal = new TransferModal();
    fromAccount.setBalance(new BigDecimal(-100));
    transferModal.setFromAccountId(fromAccount.getAccountId());
    transferModal.setToAccountId(toAccount.getAccountId());
    transferModal.setBalance(new BigDecimal(100));

    Exception exception = assertThrows(Exception.class, () -> {
      accountsService.transferBalances(transferModal);
    });
  }
}
