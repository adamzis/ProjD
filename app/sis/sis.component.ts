import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-sis',
  templateUrl: './sis.component.html',
  styleUrls: ['./sis.component.css']
})
export class SisComponent implements OnInit {
  prefix: string;
  minGpa: string;
  sortBy: string;

  status: boolean;
  submitted: boolean;
  result: string[];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.prefix = '';
    this.minGpa = '';
    this.sortBy = 'NONE';
    this.submitted = false;
  }

  onSubmit() {
    // This is ES6 backtick template literal syntax. Equivalent to string concatenation.
    const urlQuery = `http://localhost:4413/ProjF/Sis.do?prefix=${this.prefix}&minGpa=${this.minGpa}&sortBy=${this.sortBy}`;
    console.log(urlQuery);
    this.http.get(urlQuery)
      .subscribe(data => {
        const resp = JSON.parse(JSON.stringify(data));
        this.status = resp.status;
        this.result = resp.data;
      });
    this.submitted = true;
    console.log(this.result);
  }

}
