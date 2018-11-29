import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-prime',
  templateUrl: './prime.component.html',
  styleUrls: ['./prime.component.css']
})
export class PrimeComponent implements OnInit {
  min: string;
  max: string;
  result: string;

  status: boolean;
  submitted = false;

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
  }

  onSubmit() {
    this.http.get(`http://localhost:4413/ProjF/Prime.do?min=${this.min}&max=${this.max}`)
      .subscribe(data => {
        const resp = JSON.parse(JSON.stringify(data));
        this.status = resp.status;
        this.result = resp.data;
      });
    this.submitted = true;
  }

  findNext() {
    this.min = this.result;
    this.onSubmit();
  }

}
