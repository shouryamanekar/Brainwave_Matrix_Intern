package com.brainwave.atmapp.controller;

import com.brainwave.atmapp.dto.AccountDto;
import com.brainwave.atmapp.repository.AccountRepository;
import com.brainwave.atmapp.service.AccountService;
import com.brainwave.atmapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    public AccountController(AccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }


    // Shift this function somewhere else
    private String extractCardNumberFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            return jwtUtil.extractCardNumber(jwtToken);
        }
        throw new RuntimeException("Authorization token is missing or invalid.");
    }



    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> request) {
        String cardNumber = request.get("atmCardNumber");
        String pin = request.get("pin");
        String token = accountService.authenticateAccount(cardNumber, pin);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody Map<String, String> request){
        String accountHolderName = request.get("accountHolderName");
        String pin = request.get("pin");
        double initialBalance = Double.parseDouble(request.get("balance"));

        AccountDto accountDtoInput = new AccountDto(null, accountHolderName, initialBalance, null);

        AccountDto createdAccount = accountService.createAccount(accountDtoInput, pin);

        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }



    @GetMapping("/details")
    public ResponseEntity<AccountDto> getCurrentAccountDetails(HttpServletRequest request){
        String atmCardNumber = extractCardNumberFromToken(request);
        AccountDto dto = accountService.getAccountByCardNumber(atmCardNumber);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/deposit")
    public ResponseEntity<AccountDto> deposit(HttpServletRequest request, @RequestBody Map<String, Double> requestBody){
        String cardNumber = extractCardNumberFromToken(request);
        Double amount = requestBody.get("amount");
        AccountDto dto = accountService.depositByCardNumber(cardNumber, amount);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/withdraw")
    public ResponseEntity<AccountDto> withdraw(HttpServletRequest request, @RequestBody Map<String, Double> requestBody){
        String cardNumber = extractCardNumberFromToken(request);
        Double amount = requestBody.get("amount");
        AccountDto dto = accountService.withdrawByCardNumber(cardNumber, amount);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request) {
        String cardNumber = extractCardNumberFromToken(request);
        accountService.deleteAccountByCardNumber(cardNumber);
        return ResponseEntity.ok("Account Deleted");
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountDto> transferFunds(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
        String fromCardNumber = extractCardNumberFromToken(request);
        String toCardNumber = (String) requestBody.get("toCardNumber");
        Double amount = (Double) requestBody.get("amount");

        AccountDto dto = accountService.transferFunds(fromCardNumber, toCardNumber, amount);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/change-pin")
    public ResponseEntity<String> changePin(HttpServletRequest request, @RequestBody Map<String, String> requestBody) {
        String atmCardNumber = extractCardNumberFromToken(request);
        String oldPin = requestBody.get("oldPin");
        String newPin = requestBody.get("newPin");

        accountService.changePin(atmCardNumber, oldPin, newPin);
        return ResponseEntity.ok("PIN changed successfully");
    }


}
