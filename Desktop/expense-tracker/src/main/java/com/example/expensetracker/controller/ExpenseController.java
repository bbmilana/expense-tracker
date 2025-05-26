package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Показать форму добавления нового расхода
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add_expense";  // имя thymeleaf-шаблона add_expense.html
    }

    // Обработать добавление нового расхода
    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
        expenseRepository.save(expense);
        redirectAttributes.addFlashAttribute("message", "Расход успешно добавлен!");
        return "redirect:/expenses";
    }

    // Показать форму редактирования существующего расхода
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        model.addAttribute("expense", expense);
        return "add_expense";  // используем тот же шаблон add_expense.html
    }

    // Обработать обновление расхода
    @PostMapping("/edit/{id}")
    public String updateExpense(@PathVariable("id") Long id, @ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
        expense.setId(id);
        expenseRepository.save(expense);
        redirectAttributes.addFlashAttribute("message", "Расход успешно обновлён!");
        return "redirect:/expenses";
    }

    // Удалить расход
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
        expenseRepository.delete(expense);
        redirectAttributes.addFlashAttribute("message", "Расход успешно удалён!");
        return "redirect:/expenses";
    }

    // Показать список всех расходов
    @GetMapping("/expenses")
    public String listExpenses(Model model) {
        model.addAttribute("expenses", expenseRepository.findAll());
        return "expenses";  // имя thymeleaf-шаблона expenses.html
    }
}
