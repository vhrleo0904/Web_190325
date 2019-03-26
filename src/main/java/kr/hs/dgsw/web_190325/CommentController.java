package kr.hs.dgsw.web_190325;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepositry;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<Comment> list() {
        return this.commentRepositry.findAll();
    }

    @GetMapping("/view/{id}")
    public Comment view(@PathVariable Long id) {
        return this.commentRepositry.findById(id).orElse(null);
    }

    @PostMapping("add")
    public Comment add(@RequestBody Comment comment) {
        return this.commentRepositry.save(comment);
    }

    @PutMapping("/update/{id}")
    public Comment update(@PathVariable Long id, @RequestBody Comment c) {
        return this.commentRepositry.findById(id)
                .map(f -> {
                    f.setUserId(Optional.ofNullable(c.getUserId()).orElse(f.getUserId()));
                    f.setComment(Optional.ofNullable(c.getComment()).orElse(f.getComment()));
                    return this.commentRepositry.save(f);
                })
                .orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        try {
            this.commentRepositry.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/viewbyuser/{userId}")
    public List<Comment> viewByUser(@PathVariable Long userId) {
        return this.commentRepositry.findByUserId(userId);
    }

    @GetMapping("/fincbyuser/{userId}")
    public List<Comment> fincByUserId(@PathVariable Long userId) {
        this.userRepository.findById(userId)
                .map(user -> new UserCommentProtocol(user, this.commentRepositry.findByUserId(userId)))
                .orElse(null);
    }

}
